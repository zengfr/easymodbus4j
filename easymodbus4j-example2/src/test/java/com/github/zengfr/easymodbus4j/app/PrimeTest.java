package com.github.zengfr.easymodbus4j.app;

public class PrimeTest {
	public static int FindNextPrime(int i) {
		 
		int j  =i<0?1:i+1;
		int m=j%2==0?j +1:j ;
		for (;m < i * i; m +=2) {
			if (isPrime(m)) {
				return m;
			}
		} 
		return m;
	}

	public static boolean isPrime(int n) {
		if (n>2&&n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2)
			if (n % i == 0)
				return false;
		return true;
	}

	public static void main(String[] args) {
		System.out.println(FindNextPrime(0));
		System.out.println(FindNextPrime(23));
		System.out.println(FindNextPrime(2));
	}
}
