using System;
using GameScene;
using TMPro;
using UnityEngine;

namespace SkinsScene {
    public class Coins : MonoBehaviour {
        private TMP_Text _text;

        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }

        private void Update() {
            _text.text = $"Coins: {GameManager.GetInstance().DataFileManager.CurrentData.GetCoins()}";
        }
    }
}
