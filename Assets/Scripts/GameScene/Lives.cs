using TMPro;
using UnityEngine;

namespace GameScene {
    public class Lives : MonoBehaviour {
        private TMP_Text _text;
        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }

        private void Update() {
            _text.SetText($"Lives: {GameManager.GetInstance().Lives}");
        }
    }
}