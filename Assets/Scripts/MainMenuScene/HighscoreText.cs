using GameScene;
using TMPro;
using UnityEngine;

namespace MainMenuScene {
    public class HighscoreText : MonoBehaviour {
        private TMP_Text _text;

        private void Awake() {
            _text = GetComponent<TMP_Text>();
        }
        
        private void Update() {
            _text.text = $"Highscore: {GameManager.GetInstance().DataFileManager.CurrentData.GetHighScore()}";
        }
    }
}
