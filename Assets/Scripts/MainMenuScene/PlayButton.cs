using System.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace MainMenuScene {
    public class PlayButton : MonoBehaviour {
        private AudioSource _source;

        private void Awake() {
            _source = GetComponent<AudioSource>();
        }

        public void OnClick() {
            _source.Play();
            StartCoroutine(LoadLevel());
        }

        private IEnumerator LoadLevel() {
            yield return new WaitForSeconds(_source.clip.length);
            SceneManager.LoadScene("GameScene");
        }
    }
}