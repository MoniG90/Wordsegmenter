class TMP{


    public static void main(String[] args){
       int tp = 9;
       int fp = 1;
       System.out.println(calcPrecision(tp,fp));
    }

    public static double calcPrecision(int truePositives, int falsePositives) {
        return (double)truePositives/((double)truePositives+(double)falsePositives);
    }
    

}