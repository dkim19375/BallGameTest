using TMPro;
using UnityEngine;

namespace GameScene {
    public class Score : MonoBehaviour {
        private TMP_Text _text;
        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }

        private void Update() {
            _text.SetText($"Score: {GameManager.GetInstance().Score}");
        }
    }
}