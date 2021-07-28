using System;
using UnityEngine;

public class Angles {
    public enum Types {
        Up,
        Down,
        Left,
        Right,
        UpLeft,
        UpRight,
        DownLeft,
        DownRight
    }

    public static float GetAngle(Types type) => AngleBetweenVector2(new Vector2(1, 1), GetVector(type) + new Vector2(1, 1));

    public static float AngleBetweenVector2(Vector2 vec1, Vector2 vec2) {
        var vec1Rotated90 = new Vector2(-vec1.y, vec1.x);
        var sign = Vector2.Dot(vec1Rotated90, vec2) < 0 ? -1.0f : 1.0f;
        return Vector2.Angle(vec1, vec2) * sign;
    }

    public static Vector2 GetVector(Types type) {
        switch (type) {
            case Types.Up: return Vector2.up;
            case Types.Down: return Vector2.down;
            case Types.Left: return Vector2.left;
            case Types.Right: return Vector2.right;
            case Types.UpLeft: {
                var newX = Math.Cos(Mathf.Deg2Rad * 135.0);
                var newY = Math.Sin(Mathf.Deg2Rad * 135.0);
                return new Vector2((float) newX, (float) newY);
            }
            case Types.UpRight: {
                var newX = Math.Cos(Mathf.Deg2Rad * 45.0);
                var newY = Math.Sin(Mathf.Deg2Rad * 45.0);
                return new Vector2((float) newX, (float) newY);
            }
            case Types.DownLeft: {
                var newX = Math.Cos(Mathf.Deg2Rad * 225.0);
                var newY = Math.Sin(Mathf.Deg2Rad * 225.0);
                return new Vector2((float) newX, (float) newY);
            }
            case Types.DownRight: {
                var newX = Math.Cos(Mathf.Deg2Rad * 315.0);
                var newY = Math.Sin(Mathf.Deg2Rad * 315.0);
                return new Vector2((float) newX, (float) newY);
            }
            default:
                throw new ArgumentOutOfRangeException(nameof(type), type, null);
        }
    }

    public static float GetAngle(Vector2 from, Vector2 to) {
        var one = from - from;
        var second = to - from;
        return (float) (Math.Atan2(second.y - one.y, second.x - one.x) * (180 / Math.PI));
    }

    public static Vector2 GetDirectionPoint(Vector2 start, float speed, float angle) {
        var newX = start.x + speed * Math.Cos(Mathf.Deg2Rad * angle);
        var newY = start.y + speed * Math.Sin(Mathf.Deg2Rad * angle);
        return new Vector2((float) newX, (float) newY);
    }
}