using System;
using GameScene;

namespace Data {
    [Serializable]
    public struct GameData {
        private int _coins;
        private int _highScore;

        public GameData(int coins = 0, int highScore = 0) {
            _coins = coins;
            _highScore = highScore;
        }

        public int GetCoins() => _coins;

        public void SetCoins(int newCoins) {
            _coins = newCoins;
            GameManager.GetInstance().DataFileManager.Save(this);
        }
        
        public int GetHighScore() => _highScore;

        public void SetHighScore(int newHighScore) {
            _highScore = newHighScore;
            GameManager.GetInstance().DataFileManager.Save(this);
        }

        public override string ToString() => $"GameData{{_coins={_coins}, _highScore={_highScore}}}";
    }
}