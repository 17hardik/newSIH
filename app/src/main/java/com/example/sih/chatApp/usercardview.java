package com.example.sih.chatApp;


public class usercardview {
    private String Username;
    private String Name;
    private String Phone;

    public usercardview() {
    }

    public usercardview(String name, String phone, String username) {
       Username = username;
       Name = name;
       Phone = phone;
    }

    public String getUsername() {
        String username = decryptUsername(Username).toString();
        return username;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public StringBuilder decryptUsername(String uname) {
        int pllen;
        StringBuilder sb = new StringBuilder();
        int ciplen = uname.length();

        String temp = Character.toString(uname.charAt(ciplen - 2));
        if (temp.matches("[a-z]+")) {
            pllen = Character.getNumericValue(uname.charAt(ciplen - 1));
            pllen -= 2;
        } else {
            String templen = uname.charAt(ciplen - 2) + Character.toString(uname.charAt(ciplen - 1));
            pllen = Integer.parseInt(templen);
            pllen -= 2;
        }
        String[] separated = uname.split("[a-zA-Z]");
        for (int i = 0; i < pllen; i++) {
            String splitted = separated[i];
            int split = Integer.parseInt(splitted);
            split = split + pllen + (2 * i);
            char pln = (char) split;
            sb.append(pln);
        }
        return sb;
    }

}