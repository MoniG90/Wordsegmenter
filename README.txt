1. To create our bigram and Trie : java Main <dictionary> <korpus1> <korpus2> <threshold>
   example : java Main dictionary korpus1 korpus2 0.00011
   0.00011 is the optimal value for treshold

2. To create Test Data : java FormatTest readTestText.txt ROWS WORD_EACH_ROW <untreated/treated>
   This java program will write to "testText.txt" and "testTextFacit.txt"
   example : java FormatTest readTestText.txt 10 8 untreated

3. To calculate complexity : java CalculateTime > an_out_put_file

4. To Evaluate : java Evaluate ROWS
   Will evaluate on testTextFacit.txt and testText.txt
    example : java Evaluate 1000
    will evaluate 1000 rows

    

