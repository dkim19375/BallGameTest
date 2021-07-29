using UnityEngine;

namespace GameScene {
    public class CameraFollow : MonoBehaviour {
        public Transform target;
        private const float MaximumOut = 2.5f;

        private void FixedUpdate() {
            Follow();
        }

        private void Follow() {
            var targetPos = target.position;
            
            var newX = Mathf.Clamp(targetPos.x, -MaximumOut, MaximumOut);
            var newY = Mathf.Clamp(targetPos.y, -MaximumOut, MaximumOut);
            // ReSharper disable once Unity.InefficientPropertyAccess
            transform.position = new Vector3(newX, newY, transform.position.z);
        }
    }
}