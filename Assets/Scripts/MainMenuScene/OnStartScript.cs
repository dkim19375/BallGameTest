using UnityEngine;

namespace MainMenuScene {
    public class OnStartScript : MonoBehaviour {
        private static bool _started = false;

        private void Start() {
            if (_started) {
                return;
            }
            _started = true;
            OnStart();
        }

        private void OnStart() {
            Screen.fullScreen = true;
        }
    }
}
