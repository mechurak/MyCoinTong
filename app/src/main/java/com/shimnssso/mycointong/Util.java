package com.shimnssso.mycointong;

public class Util {
    public static String getCurrenySymbol(String currency) {
        switch (currency) {
            case Const.Currency.KRW :
                return "₩";
            case Const.Currency.USD :
                return "$";
            case Const.Currency.CNY :
                return "¥";
            case Const.Currency.JPY :
                return "¥";
        }
        return "";
    }

    public static boolean useIntCurrency(String currency) {
        switch (currency) {
            case Const.Currency.KRW :
            case Const.Currency.JPY :
                return true;
            case Const.Currency.USD :
            case Const.Currency.CNY :
                return false;
        }
        return false;
    }
}
