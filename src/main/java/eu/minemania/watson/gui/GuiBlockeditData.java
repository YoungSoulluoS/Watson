package eu.minemania.watson.gui;

import java.util.List;
import javax.annotation.Nullable;
import eu.minemania.watson.db.BlockEdit;
import eu.minemania.watson.db.data.BlockeditBase;
import eu.minemania.watson.db.data.BlockeditEntry;
import eu.minemania.watson.db.data.WidgetBlockeditEntry;
import eu.minemania.watson.db.data.WidgetListBlockedit;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screen.Screen;

public class GuiBlockeditData extends GuiListBase<BlockeditEntry, WidgetBlockeditEntry, WidgetListBlockedit>
implements ICompletionListener
{
    protected final BlockeditBase display;

    public GuiBlockeditData(BlockeditBase display, String titleKey, List<BlockEdit> blockedit, @Nullable Screen parent)
    {
        super(12, 30);

        this.setParent(parent);
        this.display = display;
        this.title = StringUtils.translate(titleKey);
        this.useTitleHierarchy = false;
        this.setBlitOffset(1);

        WidgetBlockeditEntry.setMaxNameLength(display.getBlockeditAll());
    }

    @Override
    protected WidgetListBlockedit createListWidget(int listX, int listY)
    {
        return new WidgetListBlockedit(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), this);
    }

    @Override
    protected int getBrowserWidth()
    {
        return this.width - 20;
    }

    @Override
    protected int getBrowserHeight()
    {
        return this.height - 80;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int x = 12;
        int y = this.height - 26;
        int buttonWidth = getButtonWidth();

        this.createButton(x, y, buttonWidth, ButtonType.CLOSE);
    }

    protected int getButtonWidth()
    {
        int width = 0;

        for(ButtonType type : ButtonType.values())
        {
            width = Math.max(width, this.getStringWidth(type.getDisplayName()) + 10);
        }

        return width;
    }

    protected void createButton(int x, int y, int buttonWidth, ButtonType type)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, buttonWidth, 20, type.getDisplayName());
        this.addButton(button, this.createActionListener(type));
    }

    @Override
    public boolean isPauseScreen()
    {
        return this.getParent() != null && this.getParent().isPauseScreen();
    }

    public BlockeditBase getDisplay()
    {
        return this.display;
    }

    protected ButtonListener createActionListener(ButtonType type)
    {
        return new ButtonListener(type, this);
    }

    protected static class ButtonListener implements IButtonActionListener
    {
        private final GuiBlockeditData gui;
        private final ButtonType type;

        public ButtonListener(ButtonType type, GuiBlockeditData gui)
        {
            this.type = type;
            this.gui = gui;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            if (this.type == ButtonType.CLOSE)
            {
                GuiBase.openGui(this.gui.getParent());
            }

        }
    }

    protected enum ButtonType
    {
        CLOSE  ("watson.gui.button.blockedit.close");

        private final String labelKey;

        private ButtonType(String labelKey)
        {
            this.labelKey = labelKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.labelKey);
        }
    }

    @Override
    public void onTaskCompleted()
    {
        if(GuiUtils.getCurrentScreen() == this)
        {
            WidgetBlockeditEntry.setMaxNameLength(this.display.getBlockeditAll());
            this.initGui();
        }
    }
}