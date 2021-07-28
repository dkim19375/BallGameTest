using TMPro;
using UnityEngine;

namespace GameScene {
    public class Coins : MonoBehaviour {
        private TMP_Text _text;
        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }

        private void Update() {
            _text.SetText($"Coins: {GameManager.GetInstance().CollectedCoins}");
        }
    }
}