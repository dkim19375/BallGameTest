using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using GameScene;
using UnityEngine;
using UnityEngine.UI;

public delegate RawImage GetRawImage(RawImage input);

[SuppressMessage("ReSharper", "MemberCanBePrivate.Global")]
public class SkinType {
        
    public static readonly SkinType None = new SkinType(0, rawImage => {
        rawImage.color = new Color32(60, 179, 113, 255);
        rawImage.texture = Utilities.RoundCrop(Resources.Load<Texture2D>("Images/Circle"));
        return rawImage;
    });

    public static readonly SkinType CustomSkin = new SkinType(300,
        rawImage => {
            var customSkin = GameManager.GetInstance().DataFileManager.CurrentData.GetCustomSkin();
            rawImage.texture = Utilities.RoundCrop(Resources.Load<Texture2D>(customSkin));
            return rawImage;
        }
    );
    
    public static readonly SkinType One = GetFromSkinNum(10, 1);
    public static readonly SkinType Two = GetFromSkinNum(30, 2);
    public static readonly SkinType Three = GetFromSkinNum(50, 3);
    public static readonly SkinType Four = GetFromSkinNum(80, 4);
    public static readonly SkinType Five = GetFromSkinNum(100, 5);
    public static readonly SkinType Six = GetFromSkinNum(200, 6);
    
    [SuppressMessage("ReSharper", "StringLiteralTypo")]
    private static readonly IDictionary<string, SkinType> Values = new Dictionary<string, SkinType> {
        { "none", None },
        { "customskin", CustomSkin },
        { "one", One },
        { "two", Two },
        { "three", Three },
        { "four", Four },
        { "five", Five },
        { "six", Six },
    };

    public readonly GetRawImage Image;
    public readonly int Price;

    private SkinType(int price, GetRawImage image) {
        Image = image;
        Price = price;
    }

    public string GetName() => Values.Keys.FirstOrDefault(name => Values[name] == this);

    private static SkinType GetFromSkinNum(int price, int num) => new SkinType(price,
        rawImage => {
            rawImage.texture = Utilities.RoundCrop(Resources.Load<Texture2D>($"Images/Skins/{num}"));
            return rawImage;
        }
    );

    [SuppressMessage("ReSharper", "StringLiteralTypo")]
    public static SkinType GetFromName(string name) {
        return name.ToLower() switch {
            "1" => One,
            "2" => Two,
            "3" => Three,
            "4" => Four,
            "5" => Five,
            "6" => Six,
            _ => Values[name.ToLower()]
        };
    }
}