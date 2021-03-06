package com.t2xm.utils;

import android.content.res.XmlResourceParser;

import com.t2xm.entity.Item;
import com.t2xm.utils.valuesConverter.JsonUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemXmlParser {
    public static List<Item> parseItems(XmlResourceParser parser) throws XmlPullParserException, IOException {
        List<Item> itemList = new ArrayList<>();
        int eventType = parser.getEventType();
        Item item = null;
        List<String> imageNameList = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Items")) {
                        break;
                    } else if (parser.getName().equals("Item")) {
                        item = new Item();
                    } else if (parser.getName().equals("itemName")) {
                        item.itemName = parser.nextText().trim();
                    } else if (parser.getName().equals("category")) {
                        item.category = Integer.valueOf(parser.nextText().trim());
                    } else if (parser.getName().equals("description")) {
                        item.description = parser.nextText().trim();
                    } else if (parser.getName().equals("phoneNumber")) {
                        item.phoneNumber = parser.nextText().trim();
                    } else if(parser.getName().equals("officialWebsite")) {
                        item.officialWebsite = parser.nextText().trim();
                    } else if(parser.getName().equals("travelInformationWebsite")) {
                        item.travelInformationWebsite = parser.nextText().trim();
                    } else if (parser.getName().equals("images")) {
                        imageNameList = new ArrayList<>();
                    } else if (parser.getName().equals("image")) {
                        imageNameList.add(parser.nextText().trim());
                    } else if (parser.getName().equals("longitude")) {
                        item.longitude = Double.valueOf(parser.nextText().trim());
                    } else if (parser.getName().equals("latitude")) {
                        item.latitude = Double.valueOf(parser.nextText().trim());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Item")) {
                        itemList.add(item);
                    } else if (parser.getName().equals("images")) {
                        item.image = JsonUtil.mapObjectToJson(imageNameList);
                    }
                    break;
            }
            eventType = parser.next();
        }

        return itemList;
    }
}
