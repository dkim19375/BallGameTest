using UnityEngine;

namespace GameScene {
    public class CameraFollow : MonoBehaviour {
        public Transform target;

        private void FixedUpdate() {
            Follow();
        }

        private void Follow() {
            var targetPos = target.position;
            
            // ReSharper disable once Unity.InefficientPropertyAccess
            transform.position = new Vector3(targetPos.x, targetPos.y, transform.position.z);
        }
    }
}