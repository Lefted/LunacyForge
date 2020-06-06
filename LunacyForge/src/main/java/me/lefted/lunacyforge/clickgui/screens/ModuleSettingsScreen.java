package me.lefted.lunacyforge.clickgui.screens;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ComboInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.KeybindInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.clickgui.elements.ContainerColorpicker;
import me.lefted.lunacyforge.clickgui.elements.ContainerComobox;
import me.lefted.lunacyforge.clickgui.elements.ContainerKeybind;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider;
import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.utils.AnnotationUtils;
import me.lefted.lunacyforge.valuesystem.Relation;
import me.lefted.lunacyforge.valuesystem.RelationManager;
import me.lefted.lunacyforge.valuesystem.Value;

public class ModuleSettingsScreen extends SettingsScreen {

    // INSTANCE
    public static ModuleSettingsScreen instance;

    // ATTRIBUTES
    private Module module;
    private BackButton backButton;

    private HashMap<Element, Relation> elementRelationMap;

    // METHODS
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	if (module != null) {
	    // add module name and description
	    addModuleInfo(settings);

	    // clear the old children list
	    RelationManager.instance.clearMap();

	    // clear the old updateChilrenMap
	    if (elementRelationMap == null) {
		elementRelationMap = new HashMap<Element, Relation>();
	    } else {
		elementRelationMap.clear();
	    }

	    // adds values like sliders, comboboxes, keybinds, checkboxes
	    addModuleValues(settings);

	    // update relations using the element relation map
	    updateRelations();
	}
    }

    @Override
    public void initOtherElements() {
	// pass call
	super.initOtherElements();

	backButton = new BackButton();
	backButton.setCallback(() -> mc.displayGuiScreen(SearchScreen.instance));
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// pass call
	super.drawOtherElements(mouseX, mouseY, partialTicks);

	// back button
	backButton.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
	super.updateScreen();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// pass call
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// if rightclick go back to search
	if (mouseButton == 1) {
	    mc.displayGuiScreen(SearchScreen.instance);
	} else {
	    // back button
	    backButton.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public boolean isUseInventoryMove() {
	return true;
    }

    public Module getModule() {
	return module;
    }

    // USETHIS before showing the screen
    public void changeModule(Module module) {
	this.module = module;
    }

    // adds the name and the description and the keybind
    private void addModuleInfo(ArrayList<SettingContainer> settings) {
	final SettingContainer name = new SettingContainer();
	name.centerX();
	name.setDescription(module.getName());

	final String descriptionString = getModuleDescription(module);
	if (!descriptionString.isEmpty()) {
	    name.setHoverText(descriptionString);
	}

	final ContainerKeybind keybind = new ContainerKeybind(this, 75, 16, module.getKeycode());
	keybind.setPosX(name.getPosX() + name.getWidth() - keybind.getWidth() - 10);
	keybind.setIntConsumer(keycode -> module.setKeycode(keycode));

	name.setSettingOffsetY(6);
	name.setSettingElement(keybind);

	settings.add(name);
    }

    // returns a description if the module has one
    private String getModuleDescription(Module module) {
	final ModuleInfo info = AnnotationUtils.getModuleInfo(module);
	if (info != null) {
	    return info.description();
	}
	return "";
    }

    // adds values like sliders, comboboxes, keybinds, checkboxes
    private void addModuleValues(ArrayList<SettingContainer> settings) {
	// iterate over fields
	for (final Field field : module.getClass().getDeclaredFields()) {
	    try {
		field.setAccessible(true);
		final Object obj = field.get(module);

		// if field is not value, goto next field
		if (!(obj instanceof Value)) {
		    continue;
		}

		// cast obj to value
		final Value value = (Value) obj;

		// iterate over annotations
		for (final Annotation annotation : field.getAnnotations()) {

		    // get containerinfo
		    final ContainerInfo info = field.getAnnotation(ContainerInfo.class);

		    // REMOVE
		    // // get children info
		    // final ChildrenInfo childInfo = field.getAnnotation(ChildrenInfo.class);

		    // checkboxes
		    if (annotation instanceof CheckboxInfo && value.getObject() instanceof Boolean) {
			addCheckbox(info, (CheckboxInfo) annotation, value, settings);
		    }

		    // sliders
		    if (annotation instanceof SliderInfo && value.getObject() instanceof Number) {
			addSlider(info, (SliderInfo) annotation, value, settings);
		    }

		    // comboboxes
		    if (annotation instanceof ComboInfo && value.getObject() instanceof Number) {
			addCombobox(info, (ComboInfo) annotation, value, settings);
		    }

		    // keybinds
		    if (annotation instanceof KeybindInfo && value.getObject() instanceof Number) {
			addKeybind(info, (KeybindInfo) annotation, value, settings);
		    }

		    // color pickers
		    if (annotation instanceof ColorInfo && value.getObject() instanceof float[]) {
			addColorPicker(info, (ColorInfo) annotation, value, settings);
		    }

		}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    // adds the value as checkbox to the settingslist
    private void addCheckbox(ContainerInfo cInfo, CheckboxInfo info, Value value, ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer();

	// adds this container to the map if it is necessary
	addToChildrenMap(value, container);

	container.centerX();
	container.setSettingOffsetY(7);
	container.setDescription(info.description());
	addHoverText(cInfo, container);
	tryGrouping(cInfo, container);
	final ContainerCheckbox checkbox = new ContainerCheckbox(((Boolean) value.getObject()).booleanValue());
	checkbox.setPosX(container.getPosX() + container.getWidth() - checkbox.WIDTH - 10);

	// notify children if necessary
	final Relation rel = value.getRelation();

	if (rel != null && rel.getChildrenContainerNames() != null && rel.getChildrenContainerNames().length > 0 && rel.getChildrenAvailability() != null) {
	    // complex consumer
	    checkbox.setConsumer(newValue -> {
		value.setObject(newValue);

		if (rel.getChildrenAvailability().test(newValue)) {
		    makeChildrenAvailable(value);
		} else {
		    makeChildrenUnavailable(value);
		}
	    });
	    // add to update map
	    elementRelationMap.put(checkbox, rel);

	} else {
	    // simple consumer
	    checkbox.setConsumer(newValue -> value.setObject(newValue));
	}

	// checkbox.setConsumer(newValue -> value.setObject(newValue));
	container.setSettingElement(checkbox);
	settings.add(container);
    }

    // adds the value as slider to the settingslist
    private void addSlider(ContainerInfo cInfo, SliderInfo info, Value value, ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer();
	// adds this container to the map if it is necessary
	addToChildrenMap(value, container);
	final ContainerSlider slider = new ContainerSlider(this, info.numberType(), info.min(), info.max(), info.step());
	slider.setValue(((Number) value.getObject()).doubleValue());
	container.centerX();
	slider.setPosX(container.getPosX() + container.getWidth() - slider.WIDTH - 5);
	slider.setConsumer(newValue -> consumSliderValue(newValue, value, slider, container, info));
	container.setSettingOffsetY(10);
	container.setDescription(info.description() + ": " + slider.getValueString());
	addHoverText(cInfo, container);
	tryGrouping(cInfo, container);
	container.setSettingElement(slider);
	settings.add(container);
    }

    // adds the value as combobox to the settingslist
    private void addCombobox(ContainerInfo cInfo, ComboInfo info, Value value, ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer();
	final ContainerComobox combobox = new ContainerComobox(this, container, ((Number) value.getObject()).intValue(), info.entryNames());
	// adds this container to the map if it is necessary
	addToChildrenMap(value, container);

	container.centerX();
	combobox.setPosX(container.getPosX() + container.getWidth() - combobox.ENTRY_WIDTH - 10);
	container.setDescription(info.description());
	combobox.setIntConsumer(newValue -> consumeIntegerValue(newValue, value));
	container.centerX();
	container.setSettingOffsetY(7);
	addHoverText(cInfo, container);
	tryGrouping(cInfo, container);
	container.setSettingElement(combobox);
	settings.add(container);
    }

    // adds the value as keybind to the settingslist
    private void addKeybind(ContainerInfo cInfo, KeybindInfo info, Value value, ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer();
	final ContainerKeybind keybind = new ContainerKeybind(this, 128, 20, ((Number) value.getObject()).intValue());
	// adds this container to the map if it is necessary
	addToChildrenMap(value, container);

	container.centerX();
	keybind.setPosX(container.getPosX() + container.getWidth() - keybind.getWidth() - 10);
	container.setDescription(info.description());

	keybind.setIntConsumer(newValue -> consumeIntegerValue(newValue, value));
	container.setSettingOffsetY(7);
	addHoverText(cInfo, container);
	tryGrouping(cInfo, container);
	container.setSettingElement(keybind);
	settings.add(container);
    }

    // adds the value as colorpicker to the settingslist
    private void addColorPicker(ContainerInfo cInfo, ColorInfo info, Value value, ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer();
	final ContainerColorpicker picker = new ContainerColorpicker(this, container, (float[]) value.getObject(), null);
	// adds this container to the map if it is necessary
	addToChildrenMap(value, container);

	container.centerX();
	picker.setPosX(container.getPosX() + container.getWidth() - picker.getWidth() - 71);
	container.setDescription(info.description());

	picker.setRGBAConsumer(rgba -> value.setObject(rgba));

	container.setSettingOffsetY(10);
	addHoverText(cInfo, container);
	tryGrouping(cInfo, container);
	container.setSettingElement(picker);
	settings.add(container);
    }

    // adds the hover text if possible to the container
    private void addHoverText(ContainerInfo cInfo, SettingContainer container) {
	if (cInfo != null && !cInfo.hoverText().isEmpty()) {
	    container.setHoverText(cInfo.hoverText());
	}
    }

    // tries to group the container if the annotation says to do so
    private void tryGrouping(ContainerInfo cInfo, SettingContainer container) {
	// TODO group only if visible
	if (cInfo != null && cInfo.groupID() >= 0) {
	    groupSettings(cInfo.groupID(), container);
	}
    }

    // adds the container to the children id map if possible
    private void addToChildrenMap(Value value, SettingContainer container) {
	final Relation childrenObj = value.getRelation();
	// if it has a children object
	if (childrenObj != null) {
	    // register it in the map
	    RelationManager.instance.addContainerToMap(childrenObj.getContainerName(), container);
	}
    }

    // makes children available (shown) if possible
    private void makeChildrenAvailable(Value value) {
	final Relation childrenObj = value.getRelation();

	if (childrenObj != null && childrenObj.getChildrenContainerNames() != null && childrenObj.getChildrenContainerNames().length > 0) {

	    for (String name : childrenObj.getChildrenContainerNames()) {

		final SettingContainer container = RelationManager.instance.getContainerByName(name);
		if (container != null) {
		    setSettingContainersAvailability(container, true);
		}
	    }
	}
    }

    // makes children unavailable (hidden) if possible
    private void makeChildrenUnavailable(Value value) {
	final Relation childrenObj = value.getRelation();

	if (childrenObj != null && childrenObj.getChildrenContainerNames() != null && childrenObj.getChildrenContainerNames().length > 0) {

	    for (String name : childrenObj.getChildrenContainerNames()) {

		final SettingContainer container = RelationManager.instance.getContainerByName(name);
		if (container != null) {
		    setSettingContainersAvailability(container, false);
		}
	    }
	}
    }

    // called one when constructing the containers
    private void updateRelations() {
	final Iterator it = elementRelationMap.keySet().iterator();

	while (it.hasNext()) {
	    final Element element = (Element) it.next();

	    if (element instanceof ContainerCheckbox) {
		final ContainerCheckbox checkbox = (ContainerCheckbox) element;
		final Relation rel = elementRelationMap.get(checkbox);

		for (String name : rel.getChildrenContainerNames()) {
		    final SettingContainer child = RelationManager.instance.getContainerByName(name);
		    setSettingContainersAvailability(child, rel.getChildrenAvailability().test(checkbox.getValue()));
		}
	    }
	}
    }

    private void consumSliderValue(double newValue, Value value, ContainerSlider slider, SettingContainer container, SliderInfo info) {
	final Object obj = value.getObject();

	if (obj instanceof Integer) {
	    value.setObject(new Integer(new Double(newValue).intValue()));
	}
	if (obj instanceof Double) {
	    value.setObject(new Double(newValue));
	}
	if (obj instanceof Float) {
	    value.setObject(new Float(newValue));
	}
	if (obj instanceof Short) {
	    value.setObject(new Short(new Double(newValue).shortValue()));
	}
	if (obj instanceof Byte) {
	    value.setObject(new Byte(new Double(newValue).byteValue()));
	}
	if (obj instanceof Long) {
	    value.setObject(new Long(new Double(newValue).longValue()));
	}
	container.setDescription(info.description().concat(": ".concat(slider.getValueString())));
    }

    private void consumeIntegerValue(int newValue, Value value) {
	final Object obj = value.getObject();
	if (obj instanceof Integer) {
	    value.setObject(new Integer(new Double(newValue).intValue()));
	}
	if (obj instanceof Double) {
	    value.setObject(new Double(newValue));
	}
	if (obj instanceof Float) {
	    value.setObject(new Float(newValue));
	}
	if (obj instanceof Short) {
	    value.setObject(new Short(new Double(newValue).shortValue()));
	}
	if (obj instanceof Byte) {
	    value.setObject(new Byte(new Double(newValue).byteValue()));
	}
	if (obj instanceof Long) {
	    value.setObject(new Long(new Double(newValue).longValue()));
	}
    }
}
