using System;
using System.IO;
using System.Reflection;
using UnityEngine;

public static class Utilities {
    public static MethodInfo GetMethodInfo(Action a) => a.Method;

    public static Texture2D LoadPNG(string filePath) {
        if (!File.Exists(filePath)) {
            Debug.Log($"None: {new FileInfo(filePath).Directory?.FullName}");
            return null;
        }
        var fileData = File.ReadAllBytes(filePath);
        var tex = new Texture2D(2, 2, TextureFormat.BGRA32, false);
        tex.LoadImage(fileData);
        return tex;
    }

    public static Texture2D RoundCrop(Texture2D sourceTex) {
        var h = Math.Min(sourceTex.height, sourceTex.width);
        var w = Math.Min(sourceTex.height, sourceTex.width);
        var r = Math.Min(sourceTex.height, sourceTex.width) / 2f;
        var cx = sourceTex.width / 2f;
        var cy = sourceTex.height / 2f;
        var c = sourceTex.GetPixels(0, 0, sourceTex.width, sourceTex.height);
        var b = new Texture2D(h, w);
        for (var i = 0; i < h * w; i++) {
            var y = Mathf.FloorToInt(i / (float) w);
            var x = Mathf.FloorToInt(i - (float) (y * w));
            if (r * r >= (x - cx) * (x - cx) + (y - cy) * (y - cy)) {
                b.SetPixel(x, y, c[i]);
            } else {
                b.SetPixel(x, y, Color.clear);
            }
        }
        b.Apply();
        return b;
    }
}