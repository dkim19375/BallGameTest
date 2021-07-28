public class TagGame {
    // singleton
    private static readonly TagGame Singleton = new TagGame();
    public static TagGame GetInstance() => Singleton;

    public GameState State = GameState.MainMenu;
    public string Skin = "1";
}