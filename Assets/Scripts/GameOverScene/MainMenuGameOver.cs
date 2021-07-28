using UnityEngine;
using UnityEngine.SceneManagement;

namespace GameOverScene {
    public class MainMenuGameOver : MonoBehaviour {
        private AudioSource _source;
        
        private void Awake() {
            _source = GetComponent<AudioSource>();
        }
        
        public void OnClick() {
            _source.Play();
            SceneManager.LoadScene("MainMenuScene");
        }
    }
}