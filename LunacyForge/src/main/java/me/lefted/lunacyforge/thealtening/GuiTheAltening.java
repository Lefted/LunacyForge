package me.lefted.lunacyforge.thealtening;

import java.io.IOException;

import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;

import me.lefted.lunacyforge.utils.LoginUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiTheAltening extends GuiScreen {

    // ATTRIBUTES
    private GuiScreen prevScreen;

    private GuiTextField fieldApiToken;
    private GuiButton btnGenerateAccount;
    private GuiButton btnGoBack;
    private GuiButton btnServiceType;
    private String accountName = "none";
    private String accountUuid = "none";
    private String hasLabyCape = "unkown";
    private String has5ZigCape = "unkown";
    // TODO rework, it's ugly this way
    private boolean status = false;

    // CONSTRUCTOR
    public GuiTheAltening(GuiScreen prevScreen) {
	this.prevScreen = prevScreen;
    }

    // METHODS
    @Override
    public void initGui() {
	super.initGui();

	buttonList.add(btnGenerateAccount = new GuiButton(1, width / 2 - 100, height * 2 / 3, "Generate Account"));
	buttonList.add(btnGoBack = new GuiButton(2, width / 2 - 100, height * 2 / 3 + 20 + 4, "Done"));

	buttonList.add(btnServiceType = new GuiButton(4, width / 2 - 100, height * 2 / 3 + 48, String.format("Service: %s", TheAlteningAuthentication.theAltening()
	    .getService() == AlteningServiceType.THEALTENING ? "TheAltening" : "Mojang")));

	// fieldApiToken = new GuiTextField(componentId, fontrendererObj, x, y, par5Width, par6Height);
	fieldApiToken = new GuiTextField(3, this.fontRendererObj, width / 2 - 100, height * 1 / 4 - 20 - 4, 200, 20);

	accountName = mc.getSession().getUsername();
	accountUuid = mc.getSession().getPlayerID();

	// TODO load api key from config
	// TODO update api key
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
	super.actionPerformed(button);

	if (button.id == btnGenerateAccount.id) {
	    LoginUtils.INSTACE.generateAccountFromTheAltening().thenAccept((account) -> {
		accountName = account.getUsername();
		accountUuid = account.getPassword();
		hasLabyCape = account.getInfo().hasLabyModCape() ? "yes" : "no";
		has5ZigCape = account.getInfo().hasFiveZigCape() ? "yes" : "no";
		if (LoginUtils.INSTACE.login(account.getToken())) {
		    status = true;
		}
		btnServiceType.displayString = String.format("Service: %s", TheAlteningAuthentication.theAltening().getService() == AlteningServiceType.THEALTENING
		    ? "TheAltening"
		    : "Mojang");
	    });
	}

	if (button.id == btnGoBack.id) {
	    mc.displayGuiScreen(prevScreen);
	}

	if (button.id == btnServiceType.id) {
	    if (TheAlteningAuthentication.theAltening().getService() == AlteningServiceType.THEALTENING) {
		TheAlteningAuthentication.theAltening().mojang();
	    } else {
		TheAlteningAuthentication.theAltening().theAltening();
	    }
	    btnServiceType.displayString = String.format("Service: %s", TheAlteningAuthentication.theAltening().getService() == AlteningServiceType.THEALTENING
		? "TheAltening"
		: "Mojang");
	}

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	super.mouseClicked(mouseX, mouseY, mouseButton);
	this.fieldApiToken.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);

	if (this.fieldApiToken.isFocused()) {
	    this.fieldApiToken.textboxKeyTyped(typedChar, keyCode);
	}

	// update api key
	LoginUtils.INSTACE.updateTheAlteningApiKey(fieldApiToken.getText());
    }

    @Override
    public void updateScreen() {
	super.updateScreen();

	fieldApiToken.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	if (mc.theWorld == null) {
	    drawDefaultBackground();
	}

	for (int i = 0; i < this.buttonList.size(); ++i) {
	    ((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
	}
	fieldApiToken.drawTextBox();

	// username
	drawString(mc.fontRendererObj, String.format("Username: %s", accountName), width / 2 - 100, height * 1 / 4, -6250336);
	// uuid
	drawString(mc.fontRendererObj, String.format("uuid: %s", accountUuid), width / 2 - 100, height * 1 / 4 + 15, -6250336);
	// labycape
	drawString(mc.fontRendererObj, String.format("Labymod cape: %s", hasLabyCape), width / 2 - 100, height * 1 / 4 + 30, -6250336);
	// 5zigcape
	drawString(mc.fontRendererObj, String.format("5Zig cape: %s", has5ZigCape), width / 2 - 100, height * 1 / 4 + 45, -6250336);
	// status
	drawString(mc.fontRendererObj, "Status:", width / 2 - 100, height * 1 / 4 + 60, -6250336);
	drawString(mc.fontRendererObj, status ? "Logged in" : "", width / 2 - 100 + 40, height * 1 / 4 + 60, 0x00FF00);
	// api
	if (fieldApiToken.getText().isEmpty() && !fieldApiToken.isFocused()) {
	    drawString(mc.fontRendererObj, "Api-Token", width / 2 - 100 + 5, height * 1 / 4 - 20 - 4 + 7, -6250336);
	}
    }

    @Override
    public void onGuiClosed() {
	super.onGuiClosed();

	// if (future != null) {
	// if (!future.isDone() && !future.isCompletedExceptionally()) {
	// Logger.logErrConsole("Screen closed to early");
	// future.completeExceptionally(new Throwable("Aborted fetching account from TheAltening"));
	// }
	// }

	// TODO save api key
    }
}
