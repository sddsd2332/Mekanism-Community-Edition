package mekanism.client;

import mekanism.api.EnumColor;
import mekanism.common.Mekanism;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class HolidayManager {

    private static Calendar calendar = Calendar.getInstance();
    private static Minecraft mc = Minecraft.getMinecraft();

    private static List<Holiday> holidays = new ArrayList<>();
    private static List<Holiday> holidaysNotified = new ArrayList<>();

    public static final Holiday MAY_4 = new May4();
    public static final Holiday APRIL_FOOLS = new AprilFools();

    public static void init() {
        if (MekanismConfig.current().client.holidays.val()) {
            holidays.add(new Christmas());
            holidays.add(new NewYear());
            holidays.add(MAY_4);
            holidays.add(APRIL_FOOLS);
        }
        LocalDate time = LocalDate.now();
        YearlyDate date = new YearlyDate(time.getMonthValue(), time.getDayOfMonth());
        for (Holiday holiday : holidays) {
            holiday.updateIsToday(date);
        }
        Mekanism.logger.info("Initialized HolidayManager.");
    }

    public static void check() {
        try {
            YearlyDate date = getDate();

            for (Holiday holiday : holidays) {
                if (!holidaysNotified.contains(holiday)) {
                    if (holiday.checkIsToday(date)) {
                        holiday.onEvent(mc.player);
                        holidaysNotified.add(holiday);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static ResourceLocation filterSound(ResourceLocation sound) {
        if (!MekanismConfig.current().client.holidays.val()) {
            return sound;
        }
        try {
            YearlyDate date = getDate();
            for (Holiday holiday : holidays) {
                if (holiday.checkIsToday(date)) {
                    return holiday.filterSound(sound);
                }
            }
        } catch (Exception ignored) {
        }
        return sound;
    }

    private static YearlyDate getDate() {
        return new YearlyDate(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String getThemedLines(EnumColor[] colors, int amount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            builder.append(colors[i % colors.length]).append("-");
        }
        return builder.toString();
    }

    public enum Month {
        JANUARY("January"),
        FEBRUARY("February"),
        MARCH("March"),
        APRIL("April"),
        MAY("May"),
        JUNE("June"),
        JULY("July"),
        AUGUST("August"),
        SEPTEMBER("September"),
        OCTOBER("October"),
        NOVEMBER("November"),
        DECEMBER("December");

        private final String name;

        Month(String n) {
            name = n;
        }

        public String getName() {
            return name;
        }

        public int month() {
            return ordinal() + 1;
        }
    }

    public abstract static class Holiday {

        public abstract YearlyDate getDate();

        public abstract void onEvent(EntityPlayer player);

        public ResourceLocation filterSound(ResourceLocation sound) {
            return sound;
        }

        private boolean isToday;

        private void updateIsToday(YearlyDate date) {
            isToday = checkIsToday(date);
        }
        protected boolean checkIsToday(YearlyDate date) {
            return getDate().equals(date);
        }

        public boolean isToday() {
            return isToday;
        }

    }

    private static class Christmas extends Holiday {

        private String[] nutcracker = new String[]{"christmas.1", "christmas.2", "christmas.3", "christmas.4", "christmas.5"};

        @Override
        public YearlyDate getDate() {
            return new YearlyDate(12, 25);
        }

        @Override
        public void onEvent(EntityPlayer player) {
            String themedLines = getThemedLines(new EnumColor[]{EnumColor.DARK_GREEN, EnumColor.DARK_RED}, 13);
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + Mekanism.LOG_TAG + themedLines));
            player.sendMessage(new TextComponentString(EnumColor.RED + LangUtils.localize("holiday.mekanism.christmas.1") + " " + EnumColor.DARK_BLUE + player.getName() + " " + EnumColor.RED + LangUtils.localize("!")));
            player.sendMessage(new TextComponentString(EnumColor.RED + LangUtils.localize("holiday.mekanism.christmas.2")));
            player.sendMessage(new TextComponentString(EnumColor.RED + LangUtils.localize("holiday.mekanism.christmas.3")));
            player.sendMessage(new TextComponentString(EnumColor.RED + LangUtils.localize("holiday.mekanism.christmas.4")));
            player.sendMessage(new TextComponentString(EnumColor.DARK_GREY + "-aidancbrady"));
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + "[=======]" + themedLines));
        }

        @Override
        public ResourceLocation filterSound(ResourceLocation sound) {
            String soundLocation = sound.toString();
            if (soundLocation.contains("machine.enrichment")) {
                return new ResourceLocation(soundLocation.replace("machine.enrichment", nutcracker[0]));
            } else if (soundLocation.contains("machine.metalinfuser")) {
                return new ResourceLocation(soundLocation.replace("machine.metalinfuser", nutcracker[1]));
            } else if (soundLocation.contains("machine.purification")) {
                return new ResourceLocation(soundLocation.replace("machine.purification", nutcracker[2]));
            } else if (soundLocation.contains("machine.smelter")) {
                return new ResourceLocation(soundLocation.replace("machine.smelter", nutcracker[3]));
            } else if (soundLocation.contains("machine.dissolution")) {
                return new ResourceLocation(soundLocation.replace("machine.dissolution", nutcracker[4]));
            }
            return sound;
        }
    }

    private static class AprilFools extends  Holiday{

        @Override
        public YearlyDate getDate() {
            return new YearlyDate(4,1);
        }

        @Override
        public void onEvent(EntityPlayer player) {

        }
    }

    private static class May4 extends Holiday {

        @Override
        public YearlyDate getDate() {
            return new YearlyDate(5, 4);
        }

        @Override
        public void onEvent(EntityPlayer player) {
            String themedLines = getThemedLines(new EnumColor[]{EnumColor.BLACK, EnumColor.GREY, EnumColor.BLACK, EnumColor.YELLOW, EnumColor.BLACK}, 15);
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + Mekanism.LOG_TAG + themedLines));
            player.sendMessage(new TextComponentString(EnumColor.GREY + LangUtils.localize("holiday.mekanism.may_4.1") + " " + EnumColor.DARK_BLUE + player.getName()));
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + "[=======]" + themedLines));
        }
    }

    private static class NewYear extends Holiday {

        @Override
        public YearlyDate getDate() {
            return new YearlyDate(1, 1);
        }

        @Override
        public void onEvent(EntityPlayer player) {
            String themedLines = getThemedLines(new EnumColor[]{EnumColor.WHITE, EnumColor.YELLOW}, 13);
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + Mekanism.LOG_TAG + themedLines));
            player.sendMessage(new TextComponentString(EnumColor.AQUA + LangUtils.localize("holiday.mekanism.new_year.1") + " " + EnumColor.DARK_BLUE + player.getName() + EnumColor.RED + LangUtils.localize("!")));
            player.sendMessage(new TextComponentString(EnumColor.AQUA + LangUtils.localize("holiday.mekanism.new_year.2")));
            player.sendMessage(new TextComponentString(EnumColor.AQUA + LangUtils.localize("holiday.mekanism.new_year.3").replaceAll("%s", String.valueOf(calendar.get(Calendar.YEAR))) + LangUtils.localize("!") + " :)"));
            player.sendMessage(new TextComponentString(EnumColor.DARK_GREY + "-aidancbrady"));
            player.sendMessage(new TextComponentString(themedLines + EnumColor.DARK_BLUE + "[=======]" + themedLines));
        }
    }

    public static class YearlyDate {

        public Month month;

        public int day;

        public YearlyDate(Month m, int d) {
            month = m;
            day = d;
        }

        public YearlyDate(int m, int d) {
            this(Month.values()[m - 1], d);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof YearlyDate && ((YearlyDate) obj).month == month && ((YearlyDate) obj).day == day;
        }

        @Override
        public int hashCode() {
            int code = 1;
            code = 31 * code + month.ordinal();
            code = 31 * code + day;
            return code;
        }
    }
}
