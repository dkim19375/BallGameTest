using System;
using UnityEngine;

namespace GameScene {
    public class AutoPosition : MonoBehaviour {
        private Vector3 _start;
        private Camera _camera;

        private void Start() {
            _start = transform.position;
            _camera = GameObject.FindGameObjectWithTag("MainCamera").GetComponent<Camera>();
        }

        private void Update() {
            var cameraPos = _camera.transform.position;
            transform.position = _start + new Vector3(cameraPos.x, cameraPos.y, _start.z);
        }
    }
}