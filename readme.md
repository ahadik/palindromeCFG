#Palindrome PCFG#
##Generation and Annotation of DNA Embedded Palindromes##

###Overview###
Palindromes have very special signifigance in DNA sequences. Recall that in English language, a palindrome is a word or series of words that reads the same direction forwards and backwards. That is, the characters form a mirror image. 

In a DNA sequence, a palindrome is not a sequence of mirrored characters but instead a sequence for which the reverse complement of the sequence reads the same as the sequence. That is, reading the sequence in the 5' to 3' direction and the sequences complementary strand in the 3' to 5' direction is exactly the same.

First, recall the rules of DNA base pairing. There are 4 nucleotide bases A,T,C and G and each pairs with its complement in the following manner

>		A-T
>		T-A
>		G-C
>		C-G

Following these rules, a simple example of a DNA palindrome would be

>		ATCGAT
>		TAGCTA

Notice that reading the top strand and bottom strand of the above example in reverse directions is the same.

###Motivation###
Palindromes in DNA serve many purposes. Smaller palindromes serve as restriction sites where restriction enzymes can bind and sheer the DNA in two. Larger palindromes are the remenants of inverted duplications, where a long segment of DNA is replicated and spliced back into the genome in reverse orientation.

It's quite clear that Palindromes are of biological interest, but there are two seminal problems when in comes to computation work with palindromes. The first is the development of probabilistic model for the random generation of palindromes. The second is the use of the probabilistic model to annotate DNA sequences to identify palindromes that are statistically significant according to the provided model.

###Approach###
A common approach to this problem is the use of a Probabilistic Context Free Grammar for the generation and annotation of DNA sequences embedded with palindromes.

####Context Free Grammars####
