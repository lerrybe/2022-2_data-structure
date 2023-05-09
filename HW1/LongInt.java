// LongInt ADT for unbounded integers
import java.util.ArrayList;

public class LongInt {
    static final String POSITIVE = "positive";
    static final String NEGATIVE = "negative";

    int size;
    String sign;
    String stringLongInt;
    ArrayList<Integer> nums;

    // constructor
    public LongInt(String s) {
        this.stringLongInt = s;
        if (s.equals("-0")) {
            this.stringLongInt = "0";
        }
        this.sign = s.charAt(0) == '-' ? NEGATIVE : POSITIVE;
        if (sign.equals(NEGATIVE)) {
            this.size = s.length() - 1;
            this.nums = new ArrayList<Integer>(size);
            for (int i = 1; i < s.length(); i++) {
                nums.add(s.charAt(i) - '0');
            }
        } else {
            this.size = s.length();
            this.nums = new ArrayList<Integer>(size);
            for (int i = 0; i < s.length(); i++) {
                nums.add(s.charAt(i) - '0');
            }
        }
    }

    // returns 'this' + 'opnd'; Both inputs remain intact.
    public LongInt add(LongInt opnd) {
        if (this.sign.equals(POSITIVE) && opnd.sign.equals(POSITIVE)) {
            return new LongInt(addPositives(this, opnd).stringLongInt);
        } else if (this.sign.equals(NEGATIVE) && opnd.sign.equals(NEGATIVE)) {
            LongInt resultWithoutSign = addPositives(this.toPositive(), opnd.toPositive());
            return new LongInt(resultWithoutSign.toNegative().stringLongInt);
        } else {
            int result = findBiggerAbs(this.toPositive(), opnd.toPositive());
            LongInt biggerAbs;
            LongInt smallerAbs;
            if (result == 0) {
                return new LongInt("0");
            } else if (result > 0) {
                biggerAbs = this;
                smallerAbs = opnd;
            } else {
                biggerAbs = opnd;
                smallerAbs = this;
            }

            LongInt resultWithoutSign = subtractBiggerToSmaller(biggerAbs, smallerAbs);
            if (biggerAbs.sign.equals(NEGATIVE)) return new LongInt("-" + resultWithoutSign.stringLongInt);
            else return new LongInt(resultWithoutSign.stringLongInt);
        }
    }

    public LongInt addPositives(LongInt num1, LongInt num2) {
        int carryOn = 0;
        int biggerSize = (Math.max(num1.size, num2.size));
        ArrayList<Integer> reversedNums1 = getReverseNums(num1.nums, biggerSize);
        ArrayList<Integer> reversedNums2 = getReverseNums(num2.nums, biggerSize);
        ArrayList<Integer> reversedResultNums = new ArrayList<Integer>();

        for (int i = 0; i < biggerSize; i++) {
            int addNum = reversedNums1.get(i) + reversedNums2.get(i) + carryOn;
            reversedResultNums.add(addNum % 10);
            if (addNum > 9) carryOn = 1;
            else carryOn = 0;
        }
        if (carryOn > 0) {
            reversedResultNums.add(carryOn);
        }

        ArrayList<Integer> resultNums = getReverseNums(reversedResultNums, reversedResultNums.size());
        return new LongInt(getString(resultNums));
    }

    // returns 'this' - 'opnd'; Both inputs remain intact.
    public LongInt subtract(LongInt opnd) {
        if (this.sign.equals(POSITIVE) && opnd.sign.equals(POSITIVE)) {
            int result = findBiggerAbs(this.toPositive(), opnd.toPositive());
            LongInt biggerAbs;
            LongInt smallerAbs;
            if (result == 0) {
                return new LongInt("0");
            } else if (result > 0) {
                biggerAbs = this;
                smallerAbs = opnd;
                return subtractBiggerToSmaller(biggerAbs, smallerAbs);
            } else {
                biggerAbs = opnd;
                smallerAbs = this;
                return new LongInt("-" + subtractBiggerToSmaller(biggerAbs, smallerAbs).stringLongInt);
            }
        } else if (this.sign.equals(NEGATIVE) && opnd.sign.equals(NEGATIVE)) {
            return new LongInt(this.add(opnd.toPositive()).stringLongInt);
        } else {
            if (this.sign.equals(NEGATIVE)) {
                return this.add(opnd.toNegative());
            } else {
                return this.add(opnd.toPositive());
            }
        }
    }

    public LongInt subtractBiggerToSmaller(LongInt bigger, LongInt smaller) {
        int carryOn = 0;
        int biggerSize = (Math.max(bigger.size, smaller.size));
        ArrayList<Integer> reversedNums1 = getReverseNums(bigger.nums, biggerSize);
        ArrayList<Integer> reversedNums2 = getReverseNums(smaller.nums, biggerSize);
        ArrayList<Integer> reversedResultNums = new ArrayList<Integer>();
        ArrayList<Integer> resultNums;

        for (int i = 0; i < biggerSize; i++) {
            int subNum = reversedNums1.get(i) - reversedNums2.get(i) - carryOn;
            if (subNum >= 0) {
                reversedResultNums.add(i, subNum);
                carryOn = 0;
            } else {
                reversedResultNums.add(i, subNum + 10);
                carryOn = 1;
            }
        }

        resultNums = getReverseNums(reversedResultNums, reversedResultNums.size());
        int realSize = getRealNumsSize(reversedResultNums);

        if (realSize == resultNums.size()) {
            return new LongInt(getString(resultNums));
        } else {
            String result = getRealNumsString(getString(resultNums), realSize);
            return new LongInt(result);
        }
    }

    // returns 'this' * 'opnd'; Both inputs remain intact.
    public LongInt multiply(LongInt opnd) {
        if (this.stringLongInt.equals("0") || opnd.stringLongInt.equals("0")) {
            return new LongInt("0");
        }

        if (this.sign.equals(POSITIVE) && opnd.sign.equals(POSITIVE)) {
            return multiplyPositives(this, opnd);
        } else if (this.sign.equals(NEGATIVE) && opnd.sign.equals(NEGATIVE)) {
            return multiplyPositives(this.toPositive(), opnd.toPositive());
        } else {
            LongInt resultWithoutSign = multiplyPositives(this.toPositive(), opnd.toPositive());
            return new LongInt("-" + resultWithoutSign.stringLongInt);
        }
    }

    public LongInt multiplyPositives(LongInt num1, LongInt num2) {
        int carryOn = 0;
        ArrayList<Integer> reversedNums1 = getReverseNums(num1.nums, num1.size);
        ArrayList<Integer> reversedNums2 = getReverseNums(num2.nums, num2.size);
        LongInt resultLongInt = new LongInt("0");

        for (int i = 0; i < num2.size; i++) {
            ArrayList<Integer> reverseCurrent = new ArrayList<>(num2.size);
            for (int j = 0; j < num1.size; j++) {
                int mulNum = reversedNums1.get(j) * reversedNums2.get(i) + carryOn;
                reverseCurrent.add(mulNum % 10);
                carryOn = mulNum / 10;
            }
            if (carryOn > 0) {
                reverseCurrent.add(carryOn);
                carryOn = 0;
            }
            ArrayList<Integer> resultNums = getReverseNums(reverseCurrent, reverseCurrent.size());
            String numOfDigits = "";
            for (int j = 0; j < i; j++) {
                numOfDigits += "0";
            }
            resultLongInt = resultLongInt.add(new LongInt(getString(resultNums) + numOfDigits));
        }
        return resultLongInt;
    }

    public ArrayList<Integer> getReverseNums(ArrayList<Integer> nums, int size) {
        ArrayList<Integer> reversedNums = new ArrayList<Integer>();

        for (int i = 0; i < size; i++) {
            if (i > nums.size() - 1) {
                reversedNums.add(i, 0);
            } else {
                reversedNums.add(i, nums.get(nums.size() - 1 - i));
            }
        }
        return reversedNums;
    }

    public String getString(ArrayList<Integer> nums) {
        String result = "";
        for (int i = 0; i < nums.size(); i++) {
            result += nums.get(i);
        }
        return result;
    }

    public LongInt toPositive() {
        if (this.stringLongInt.charAt(0) == '-')
            return new LongInt(this.stringLongInt.substring(1));
        return new LongInt(this.stringLongInt);
    }

    public LongInt toNegative() {
        if (this.stringLongInt.charAt(0) != '-')
            return new LongInt("-" + this.stringLongInt);
        return new LongInt(this.stringLongInt);
    }

    public int findBiggerAbs(LongInt num1, LongInt num2) {
        if (num1.stringLongInt.equals(num2.stringLongInt)) {
            return 0;
        } else if (num1.size != num2.size) {
            return num1.size - num2.size;
        } else {
            for (int i = 0; i < size; i++) {
                if (num1.nums.get(i) == num2.nums.get(i)) continue;
                if (num1.nums.get(i) > num2.nums.get(i)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }

    public String getRealNumsString(String str, int realSize) {
        return str.substring(str.length() - realSize);
    }

    public int getRealNumsSize(ArrayList<Integer> nums) {
        int size = 0;
        for (int i = nums.size() - 1; i >= 0; i--) {
            if (nums.size() == 1 && nums.get(i) != 0) {
                size = 1;
            } else if (nums.get(i) != 0) {
                size = i + 1;
                break;
            }
        }
        return size;
    }

    // print the value of 'this' element to the standard output.
    public void print() {
        System.out.print(this.stringLongInt);
    }
}
