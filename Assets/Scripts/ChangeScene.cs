using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ChangeScene : MonoBehaviour {
    public string scene;
    private AudioSource _source;

    private void Awake() {
        _source = GetComponent<AudioSource>();
    }

    public void OnClick() {
        _source.Play();
        StartCoroutine(LoadNewScene());
    }

    private IEnumerator LoadNewScene() {
        yield return new WaitForSeconds(_source.clip.length);
        SceneManager.LoadScene(scene);
    }
}
