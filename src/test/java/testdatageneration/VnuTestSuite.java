package testdatageneration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ Vnu_Basic_Test.class, //
		Vnu_BTL_Test.class, //
		Vnu_Combined_Test.class, //
		Vnu_Namespace_Test.class, //
		Vnu_Pointer_Test.class, //
		Vnu_Pratical_Test.class, //
		Vnu_Sample1_Test.class, //
		Vnu_SwitchCase_Test.class, //
})

public class VnuTestSuite {
}