package com.shimnssso.mycointong;

public class Util {
    public static String getCurrenySymbol(String currency) {
        switch (currency) {
            case Const.Currency.KRW :
                return "₩";
            case Const.Currency.USD :
                return "$";
            case Const.Currency.CNY :
                return "Ұ";
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

    public static String priceWithSymbol(String symbol, String price) {
        if (symbol == null || price == null ) return price;
        boolean isPositive = !price.startsWith("-");
        if (isPositive) {
            return symbol + price;
        } else {
            return "-"+ symbol + price.substring(1);
        }
    }
}
