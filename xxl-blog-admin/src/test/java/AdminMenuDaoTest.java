import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xxl.core.model.main.AdminMenu;
import com.xxl.dao.IAdminMenuDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationcontext-*.xml"})
public class AdminMenuDaoTest {

	@Autowired
	private IAdminMenuDao adminMenuDao;
	
	
	@Test
	public void test() {
		List<AdminMenu> list = adminMenuDao.getMyMenus(0);
		System.out.println("-------------------------");
		System.out.println(list!=null?list.size():-1);
	}

}
