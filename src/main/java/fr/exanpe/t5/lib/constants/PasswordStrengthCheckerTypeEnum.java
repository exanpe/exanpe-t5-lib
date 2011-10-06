/**
 * 
 */
package fr.exanpe.t5.lib.constants;

/**
 * Password complexity type :
 * <ul>
 * <li>VERYWEAK : Dummy password</li><br />
 * <li>WEAK : Does not meet the minimum requirements.</li><br />
 * <li>STRONG : Meets minimum requirements.</li><br />
 * <li>STRONGEST : Check all complexity requirements.</li>
 * </ul>
 * 
 * @author lguerin
 */
public enum PasswordStrengthCheckerTypeEnum
{
    VERYWEAK, WEAK, STRONG, STRONGEST;

    public String toString()
    {
        return super.toString().toLowerCase();
    };
}
