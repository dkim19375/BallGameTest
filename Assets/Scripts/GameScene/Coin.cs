using UnityEngine;

namespace GameScene {
    public class Coin : MonoBehaviour {
        [SerializeField] private GameObject canvas;

        private readonly GameManager _gameManager = GameManager.GetInstance();
        private RectTransform _canvasTransform;
        private RectTransform _transform;
        private AudioClip _sound;
        private AudioSource _source;

        private void Awake() {
            _sound = Resources.Load("Sounds/Coin") as AudioClip;
            _source = GetComponent<AudioSource>();
            _canvasTransform = canvas.GetComponent<RectTransform>();
            _transform = GetComponent<RectTransform>();
        }

        private void Start() { 
            TeleportToRandomPoint();
            _gameManager.OnEnd.Add(this, Utilities.GetMethodInfo(OnEnd));
        }

        private void OnEnd() {
            TeleportToRandomPoint();
        }

        private void OnTriggerEnter2D(Collider2D other) {
            if (other.gameObject.GetComponent<Player>() == null) return;
            TeleportToRandomPoint();
            _gameManager.CollectedCoins += 1;
            _source.PlayOneShot(_sound, 0.6f);
        }

        public void TeleportToRandomPoint() {
            var rect = _canvasTransform.rect;
            var bounds = new Vector2(rect.width, rect.height);
            var screenX = Random.Range(-(bounds.x - 40), bounds.x - 40);
            var screenY = Random.Range(-(bounds.y - 40), bounds.y - 40);
            _transform.localPosition = new Vector3(screenX / 2, screenY / 2);
        }
    }
}