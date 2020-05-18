# SuffixAutomaton
Implementation of Suffix Automaton and its applications: LCS and finding positions of all ocurences
A suffix automaton is a powerful data structure that allows solving many string-related problems. Intuitively a suffix automaton can be understood as compressed form of all substrings of a given string. An impressive fact is, that the suffix automaton contains all this information in a highly compressed form. For a string of length n it only requires O(n) memory. Moreover, it can also be build in O(n) time (if we consider the size k of the alphabet as a constant), otherwise both the memory and the time complexity will be O(nlogk).
A suffix automaton for a given string s is a minimal DFA (deterministic finite automaton) that accepts all the suffixes of the string s.
