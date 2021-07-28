using UnityEngine;
using UnityEngine.SceneManagement;

namespace MainMenuScene {
    public class PlayButton : MonoBehaviour {
        private AudioClip _sound;
        private AudioSource _source;

        private void Awake() {
            _sound = Resources.Load("Sounds/Button") as AudioClip;
            _source = GetComponent<AudioSource>();
        }

        public void OnClick() {
            _source.PlayOneShot(_sound, 0.8f);
            SceneManager.LoadScene("GameScene");
        }
    }
}