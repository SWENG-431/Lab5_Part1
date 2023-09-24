public class ValueError extends Throwable
{
    public ValueError(int val)
    {
        super("Value " + val + " is not in any equivalence class.");
    }
}
