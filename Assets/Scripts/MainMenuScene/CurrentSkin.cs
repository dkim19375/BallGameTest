using GameScene;
using UnityEngine;
using UnityEngine.UI;

namespace MainMenuScene {
    public class CurrentSkin : MonoBehaviour {
        private RawImage _image;

        private void Awake() {
            _image = GetComponent<RawImage>();
        }

        private void Update() {
            GameManager.GetInstance().DataFileManager.CurrentData.GetCurrentSkin().Image(_image);
        }
    }
}
