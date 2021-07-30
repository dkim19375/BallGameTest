using System;
using System.Collections;
using Data;
using GameScene;
using TMPro;
using UnityEngine;
using UnityEngine.UI;
using Random = UnityEngine.Random;

namespace SkinsScene {
    public class SkinScript : MonoBehaviour {
        public string skinType;
        public TMP_Text text;
        private SkinType _skinType;
        private RawImage _image;
        public Outline outline;
        private DataFileManager DataManager => GameManager.GetInstance().DataFileManager;
        private int _current;

        private void Awake() {
            _image = GetComponent<RawImage>();
            _skinType = SkinType.GetFromName(skinType);
        }

        private void Update() {
            GameManager.GetInstance().DataFileManager.CurrentData.CheckData();
            _skinType.Image(_image);
            var coins = GameManager.GetInstance().DataFileManager.CurrentData.GetCoins();
            if (_skinType.Price <= 0) {
                text.text = "Cost: Free!";
            } else {
                text.text = $"Cost: {_skinType.Price} Coins";
            }
            if (coins >= _skinType.Price) {
                text.color = Color.green;
                return;
            }
            text.color = Color.red;
        }

        // ReSharper disable once UnusedMember.Global
        public void OnClick() {
            if (DataManager.CurrentData.GetBoughtSkins().Contains(_skinType)) {
                DataManager.CurrentData.SetCurrentSkin(_skinType);
                StartCoroutine(BorderCoroutine(Color.green));
                return;
            }
            var coins = DataManager.CurrentData.GetCoins();
            if (coins < _skinType.Price) {
                StartCoroutine(BorderCoroutine(Color.red));
                return;
            }
            DataManager.CurrentData.SetCoins(coins - _skinType.Price);
            DataManager.CurrentData.SetCurrentSkin(_skinType);
            var list = DataManager.CurrentData.GetBoughtSkins();
            list.Add(_skinType);
            DataManager.CurrentData.SetBoughtSkins(list);
            StartCoroutine(BorderCoroutine(Color.green));
        }

        private IEnumerator BorderCoroutine(Color32 color) {
            var num = Random.Range(int.MinValue, int.MaxValue);
            _current = num;
            outline.effectColor = color;
            outline.effectDistance = new Vector2(3, 3);
            yield return new WaitForSeconds(1);
            if (_current != num) {
                yield break;
            }
            outline.effectColor = new Color32(0, 0, 0, 0);
            outline.effectDistance = Vector2.zero;
        }
    }
}
