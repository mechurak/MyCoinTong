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

/*
12-16 22:49:02.987 5002-5002/com.shimnssso.mycointong E/UsdJpy: title: USDJPY=X : Summary for USD/JPY - Yahoo Finance
12-16 22:49:02.992 5002-5002/com.shimnssso.mycointong E/UsdKrw: title: USDKRW=X : Summary for USD/KRW - Yahoo Finance
12-16 22:49:04.988 5002-5002/com.shimnssso.mycointong E/UsdJpy: title: USDJPY=X 112.577 0.214 0.190% : USD/JPY - Yahoo Finance
12-16 22:49:04.997 5002-5002/com.shimnssso.mycointong E/UsdKrw: title: USDKRW=X 1,087.45 -0.14 -0.01% : USD/KRW - Yahoo Finance
*/
    public static float getExchangeRateFromYahooTitle(String rawTitle) {
        if (rawTitle == null) return 0.0f;
        rawTitle = rawTitle.replaceAll(",", "");
        String[] temp = rawTitle.split(" ");
        if (temp.length < 2) return 0.0f;
        try {
            return Float.parseFloat(temp[1]);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }
}
