import subprocess

for i in range(1, 100):
    threshold = i/100000
    threshold_with_decimals = format(threshold, ".5f")
    subprocess.call(["java", "Main", "dictionary", "korpus1", "korpus2", str(threshold_with_decimals)])
    p = subprocess.Popen(["java", "Evaluate", "1000"], stdout=subprocess.PIPE)
    correctSegmentations, err = p.communicate()
    f = open('filen_som_alla_vill_ha.txt', 'a')
    f.write("threshold_with_decimals: " + str(threshold_with_decimals) + " gave: " + str(correctSegmentations) + "\n")

f.close()    
