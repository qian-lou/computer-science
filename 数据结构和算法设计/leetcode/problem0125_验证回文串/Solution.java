class Solution {
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            while (left < right && !isNumber(s.charAt(left)) && !isLetter(s.charAt(left))) {
                left++;
            }
            while (left < right && !isNumber(s.charAt(right)) && !isLetter(s.charAt(right))) {
                right--;
            }
            if (left >= right) {
                return true;
            }
            if (isEqualChar(s.charAt(left), s.charAt(right))) {
                left++;
                right--;
            } else {
                return false;
            }
        }
        return true;
    }
    public boolean isEqualChar(char c1, char c2) {
        if (isNumber(c1) && isNumber(c2)) {
            return c1 == c2;
        }
        if (isLetter(c1) && isLetter(c2)) {
            return c1 == c2 || Math.abs(c1 - c2) == 32;
        }
        return false;
    }
    public boolean isNumber(char c) {
        return c >= 48 && c <= 57;
    }
    public boolean isLetter(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }
}