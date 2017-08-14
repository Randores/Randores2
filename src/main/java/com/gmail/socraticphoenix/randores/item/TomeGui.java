/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.randores.item;

import com.gmail.socraticphoenix.randores.RandoresKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TomeGui extends GuiScreen {
    public static final ResourceLocation BACKGROUND = new ResourceLocation("randores:textures/gui/tome.png");
    public static final ResourceLocation ICONS = new ResourceLocation("randores:textures/gui/tome_buttons.png");
    private static int[][] nextOffsets = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {0, 8}, {0, 9}, {0, 10}, {0, 11}, {0, 12}, {0, 13}, {0, 14}, {0, 15}, {0, 16}, {0, 17}, {0, 18}, {0, 19}, {0, 20}, {0, 21}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {1, 8}, {1, 9}, {1, 10}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16}, {1, 17}, {1, 18}, {1, 19}, {1, 20}, {1, 21}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {2, 14}, {2, 15}, {2, 16}, {2, 17}, {2, 18}, {2, 19}, {2, 20}, {2, 21}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {3, 14}, {3, 15}, {3, 16}, {3, 17}, {3, 18}, {3, 19}, {3, 20}, {3, 21}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {4, 16}, {4, 17}, {4, 18}, {4, 19}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {5, 16}, {5, 17}, {5, 18}, {5, 19}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16}, {6, 17}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16}, {7, 17}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {12, 10}, {12, 11}, {13, 10}, {13, 11}};
    private static int[][] backOffsets = {{0, 10}, {0, 11}, {1, 10}, {1, 11}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16}, {6, 17}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16}, {7, 17}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {8, 16}, {8, 17}, {8, 18}, {8, 19}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {9, 16}, {9, 17}, {9, 18}, {9, 19}, {10, 0}, {10, 1}, {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 16}, {10, 17}, {10, 18}, {10, 19}, {10, 20}, {10, 21}, {11, 0}, {11, 1}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {11, 6}, {11, 7}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {11, 14}, {11, 15}, {11, 16}, {11, 17}, {11, 18}, {11, 19}, {11, 20}, {11, 21}, {12, 0}, {12, 1}, {12, 2}, {12, 3}, {12, 4}, {12, 5}, {12, 6}, {12, 7}, {12, 8}, {12, 9}, {12, 10}, {12, 11}, {12, 12}, {12, 13}, {12, 14}, {12, 15}, {12, 16}, {12, 17}, {12, 18}, {12, 19}, {12, 20}, {12, 21}, {13, 0}, {13, 1}, {13, 2}, {13, 3}, {13, 4}, {13, 5}, {13, 6}, {13, 7}, {13, 8}, {13, 9}, {13, 10}, {13, 11}, {13, 12}, {13, 13}, {13, 14}, {13, 15}, {13, 16}, {13, 17}, {13, 18}, {13, 19}, {13, 20}, {13, 21}};
    public final int ySize = 166;
    public final int xSize = 247;
    private List<Element> pages;
    private boolean cleaned;
    private int currentPage;
    private RenderItem renderItem;

    public TomeGui(List<Element> pages) {
        this.pages = pages;
        this.currentPage = 0;
        if (pages.isEmpty()) {
            this.pages.add(new StringElement(TextFormatting.RED + "Error: no pages present", RandoresKeys.ERROR));
        }
    }

    public Element getPage() {
        return this.pages.get(this.currentPage);
    }

    public boolean hasNext() {
        return this.currentPage < this.pages.size() - 1;
    }

    public boolean hasPrevious() {
        return this.currentPage - 1 >= 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!this.cleaned) {
            this.cleaned = true;
            this.pages = this.clean(this.pages);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        this.mc.getTextureManager().bindTexture(ICONS);
        {
            int bx = 5 + i;
            int by = 139 + j;
            int width = 14;
            int height = 22;
            int tx;
            int ty;

            if (this.previousButtonContains(bx, by, mouseX, mouseY)) {
                tx = 34;
                ty = 37;
            } else {
                tx = 34;
                ty = 5;
            }

            this.drawTexturedModalRect(bx, by, tx, ty, width, height);
        }

        {
            int bx = 229 + i;
            int by = 139 + j;
            int width = 14;
            int height = 22;
            int tx;
            int ty;

            if (this.nextButtonContains(bx, by, mouseX, mouseY)) {
                tx = 16;
                ty = 37;
            } else {
                tx = 16;
                ty = 5;
            }

            this.drawTexturedModalRect(bx, by, tx, ty, width, height);
        }

        String p = (this.currentPage + 1) + "/" + this.pages.size();
        this.fontRenderer.drawSplitString(p, (this.xSize / 2) + i - this.fontRenderer.getStringWidth(p) / 2, 139 + j + (22 - this.fontRenderer.FONT_HEIGHT) / 2, Integer.MAX_VALUE, Color.BLACK.getRGB());
        this.getPage().draw(i, j, this, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        super.mouseReleased(mouseX, mouseY, state);
        if (this.previousButtonContains(5 + i, 139 + j, mouseX, mouseY)) {
            if (this.hasPrevious()) {
                this.currentPage--;
            } else {
                this.currentPage = this.pages.size() - 1;
            }
        } else if (this.nextButtonContains(229 + i, 139 + j, mouseX, mouseY)) {
            if (this.hasNext()) {
                this.currentPage++;
            } else {
                this.currentPage = 0;
            }
        }
    }

    public boolean nextButtonContains(int bx, int by, int x, int y) {
        int[] offset = {x - bx, y - by};
        for (int[] pixel : nextOffsets) {
            if (Arrays.equals(offset, pixel)) {
                return true;
            }
        }
        return false;
    }

    public boolean previousButtonContains(int bx, int by, int x, int y) {
        int[] offset = {x - bx, y - by};
        for (int[] pixel : backOffsets) {
            if (Arrays.equals(offset, pixel)) {
                return true;
            }
        }
        return false;
    }

    public FontRenderer getFont() {
        return this.fontRenderer;
    }

    public List<Element> clean(List<Element> pages) {
        List<List<Element>> contents = new ArrayList<List<Element>>();
        List<Element> ret = new ArrayList<Element>();
        StringBuilder cPage = new StringBuilder();
        Element previous = pages.get(0);
        contents.add(new ArrayList<Element>());
        ret.add(previous);
        contents.get(0).add(previous);
        int k = 0;

        for (int i = 1; i < pages.size(); i++) {
            Element page = pages.get(i);
            List<Element> expanded = page.expand(this);
            for (Element element : expanded) {
                ret.add(element);
                if (element.contentsTitle().equals(previous.contentsTitle())) {
                    contents.get(k).add(element);
                } else {
                    k++;
                    contents.add(new ArrayList<Element>());
                    previous = element;
                    contents.get(k).add(element);
                }
            }
        }

        int page = 2;
        cPage.append(TextFormatting.DARK_AQUA).append(I18n.format(RandoresKeys.CONTENTS)).append("\n");
        for (List<Element> content : contents) {
            cPage.append(TextFormatting.DARK_GREEN).append(content.size() == 1 ? String.valueOf(page) : page + "-" + (content.size() + page - 1)).append(": ").append(I18n.format(content.get(0).contentsTitle())).append("\n");
            page += content.size();
        }

        ret.add(0, new StringElement(cPage.substring(0, cPage.length() - 1), "%contents.null%"));
        return ret;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        this.renderItem = mc.getRenderItem();

    }

    public RenderItem getItem() {
        return this.renderItem;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void renderItem(ItemStack stack, int x, int y) {
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        this.getItem().renderItemAndEffectIntoGUI(this.mc.player, stack, x, y);
        this.getItem().renderItemOverlayIntoGUI(this.getFont(), stack, x, y, this.det(stack));
        RenderHelper.enableStandardItemLighting();
    }

    private String det(ItemStack stack) {
        return stack.getCount() == 1 ? "" : String.valueOf(stack.getCount());
    }

    public interface Element {

        void draw(int x, int y, TomeGui gui, float ticks, int mx, int my);

        List<Element> expand(TomeGui gui);

        String contentsTitle();

    }

    public static class StringElement implements Element {
        private String string;
        private String contents;

        public StringElement(String string, String contents) {
            this.string = string;
            this.contents = contents;
        }

        @Override
        public void draw(int x, int y, TomeGui gui, float ticks, int mx, int my) {
            gui.getFont().drawSplitString(this.string, x + 5, y + 5, 237, Color.BLACK.getRGB());
        }

        @Override
        public List<Element> expand(TomeGui gui) {
            List<Element> list = new ArrayList<Element>();
            if (gui.getFont().getWordWrappedHeight(this.string, 235) >= 133) {
                StringBuilder n = new StringBuilder();
                String[] pieces = this.string.split(" ");
                for (String p : pieces) {
                    p = p + " ";
                    if (gui.getFont().getWordWrappedHeight(n + p, 237) >= 133) {
                        list.add(new StringElement(n.toString(), this.contents));
                        n = new StringBuilder();
                    } else {
                        n.append(p);
                    }
                }

                if (n.length() > 0) {
                    list.add(new StringElement(n.toString(), this.contents));
                }
            } else {
                list.add(this);
            }
            return list;
        }

        @Override
        public String contentsTitle() {
            return this.contents;
        }

    }

    public static class CraftingElement implements Element {
        private static ResourceLocation CRAFTING = new ResourceLocation("randores:textures/gui/tome_crafting.png");
        private String title;
        private ItemStack[][] inventory;
        private ItemStack output;
        private String contents;

        public CraftingElement(String title, ItemStack[][] inventory, ItemStack output, String contents) {
            this.title = title;
            this.inventory = inventory;
            this.output = output;
            this.contents = contents;
        }

        @Override
        public void draw(int x, int y, TomeGui gui, float ticks, int mx, int my) {
            int taken = gui.getFont().getWordWrappedHeight(this.title, 237);
            gui.getFont().drawSplitString(this.title, x + 5, y + 5, 237, Color.BLACK.getRGB());
            y += taken + 12;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            gui.mc.getTextureManager().bindTexture(CRAFTING);
            int width = 170;
            int height = 57;
            int upperLeft = x + gui.xSize / 2 - width / 2;
            gui.drawTexturedModalRect(upperLeft, y, 0, 0, width, height);

            gui.renderItem(this.output, upperLeft + 121, y + 21);
            for (int y1 = 0; y1 < this.inventory.length; y1++) {
                ItemStack[] row = this.inventory[y1];
                for (int x1 = 0; x1 < row.length; x1++) {
                    ItemStack stack = row[x1];
                    if (stack != null && stack != ItemStack.EMPTY) {
                        int xt = upperLeft + x1 * 18 + 27;
                        int yt = y + y1 * 18 + 3;
                        gui.renderItem(stack, xt, yt);
                    }
                }
            }

        }

        @Override
        public List<Element> expand(TomeGui gui) {
            return Collections.singletonList(this);
        }

        @Override
        public String contentsTitle() {
            return this.contents;
        }

    }

    public static class FurnaceElement implements Element {
        private static ResourceLocation FURNACE = new ResourceLocation("randores:textures/gui/tome_furnace.png");
        private static ResourceLocation ICONS = new ResourceLocation("randores:textures/gui/tome_furnace_icons.png");
        private String title;
        private ItemStack fuel;
        private ItemStack in;
        private ItemStack out;

        private int tickFuelMax;
        private int tickArrowMax;

        private float tickFuel;
        private float tickArrow;

        private String contents;

        public FurnaceElement(String title, ItemStack fuel, ItemStack in, ItemStack out, String contents) {
            this.title = title;
            this.fuel = fuel;
            this.in = in;
            this.out = out;
            this.tickFuelMax = TileEntityFurnace.getItemBurnTime(fuel);
            this.tickFuelMax = this.tickFuelMax == 0 ? 150 : this.tickFuelMax;
            this.tickArrowMax = 200;
            this.contents = contents;
        }

        @Override
        public void draw(int x, int y, TomeGui gui, float ticks, int mx, int my) {
            this.tickFuel += ticks;
            this.tickArrow += ticks;
            if (this.tickFuel > this.tickFuelMax) {
                this.tickFuel = this.tickFuelMax;
            }
            if (this.tickArrow > this.tickArrowMax) {
                this.tickArrow = this.tickArrowMax;
            }
            int taken = gui.getFont().getWordWrappedHeight(this.title, 237);
            gui.getFont().drawSplitString(this.title, x + 5, y + 5, 237, Color.BLACK.getRGB());
            y += taken + 12;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            gui.mc.getTextureManager().bindTexture(FURNACE);
            int width = 167;
            int height = 57;
            int upperLeft = x + gui.xSize / 2 - width / 2;
            gui.drawTexturedModalRect(upperLeft, y, 0, 0, width, height);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            gui.mc.getTextureManager().bindTexture(ICONS);
            int bscale = this.getBurnLeftScaled(13);
            int cscale = this.getCookProgressScaled(24);
            gui.drawTexturedModalRect(upperLeft + 54, y + 34 - bscale, 0, 12 - bscale, 14, bscale + 1);
            gui.drawTexturedModalRect(upperLeft + 76, y + 20, 0, 14, cscale + 1, 16);

            if (this.in != null && this.in != ItemStack.EMPTY) {
                int xt = upperLeft + 53;
                int yt = y + 2;
                gui.renderItem(this.in, xt, yt);
            }

            if (this.fuel != null && this.fuel != ItemStack.EMPTY) {
                int xt = upperLeft + 53;
                int yt = y + 38;
                gui.renderItem(this.fuel, xt, yt);
            }

            if (this.out != null && this.out != ItemStack.EMPTY) {
                int xt = upperLeft + 113;
                int yt = y + 20;
                gui.renderItem(this.out, xt, yt);
            }

            if (this.tickFuel >= this.tickFuelMax) {
                this.tickFuel = 0;
            }
            if (this.tickArrow >= this.tickArrowMax) {
                this.tickArrow = 0;
            }
        }

        private int getCookProgressScaled(int pixels) {
            int i = (int) this.tickArrow;
            int j = this.tickArrowMax;
            return j != 0 && i != 0 ? i * pixels / j : 0;
        }

        private int getBurnLeftScaled(int pixels) {
            int i = this.tickFuelMax;

            if (i == 0) {
                i = this.tickFuelMax;
            }

            return (this.tickFuelMax - (int) this.tickFuel) * pixels / i;
        }

        @Override
        public List<Element> expand(TomeGui gui) {
            return Collections.singletonList(this);
        }

        @Override
        public String contentsTitle() {
            return this.contents;
        }

    }

}
