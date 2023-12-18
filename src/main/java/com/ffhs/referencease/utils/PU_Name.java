package com.ffhs.referencease.utils;

public class PU_Name
{
  private static String puName = "default";

  public static String getPU_Name(){
    return puName;
  }
  public static void setPU_Name(String name){
    puName = name;
  }
}
