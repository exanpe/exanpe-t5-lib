function dummyChecker(password) {
	var strength = "";
	if (password.length == 1)
	{
		strength = "veryweak";
	}	
	if (password.length == 2)
	{
		strength = "weak";
	}
	if (password.length == 3)
	{
		strength = "strong";
	}
	if (password.length > 3)
	{
		strength = "strongest";
	}
	return strength;
}
