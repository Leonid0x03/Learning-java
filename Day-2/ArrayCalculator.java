 class ArrayCalculator {
    public static int maxOfArray(int arr[]) {
		int maxValue = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > maxValue) {
				maxValue = arr[i];
			}
		}
		return maxValue;
	}

	public static double maxOfArray(double arr[]) {
		double maxValue = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > maxValue) {
				maxValue = arr[i];
			}
		}
		return maxValue;
	}

	public static int minOfArray(int arr[]) {
		int minValue = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < minValue) {
				minValue = arr[i];
			}
		}
		return minValue;
	}

	public static double minOfArray(double arr[]) {
		double minValue = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < minValue) {
				minValue = arr[i];
			}
		}
		return minValue;
	}

    public static void main(String[] args) {
        int[] intArr = {3, 7, 1, 9, 4};
        double[] doubleArr = {2.5, 8.1, 0.3, 6.7};

        System.out.println("=== Int Array ===");
        System.out.println("Max: " + maxOfArray(intArr));
        System.out.println("Min: " + minOfArray(intArr));

        System.out.println("=== Double Array ===");
        System.out.println("Max: " + maxOfArray(doubleArr));
        System.out.println("Min: " + minOfArray(doubleArr));
    }
}
