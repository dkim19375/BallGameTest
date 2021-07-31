using GameScene;
using TMPro;
using UnityEngine;

namespace MainMenuScene {
    public class CoinsText : MonoBehaviour {
        private TMP_Text _text;

        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }
        
        private void Update() {
            _text.text = $"Coins: {GameManager.GetInstance().DataFileManager.CurrentData.GetCoins()}";
        }
    }
}
