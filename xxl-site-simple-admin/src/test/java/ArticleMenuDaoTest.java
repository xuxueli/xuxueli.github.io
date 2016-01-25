import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xxl.core.model.main.ArticleMenu;
import com.xxl.core.util.JacksonUtil;
import com.xxl.dao.IArticleMenuDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationcontext-*.xml"})
public class ArticleMenuDaoTest {

	@Autowired
	private IArticleMenuDao articleMenuDao;
	
	
	@Test
	public void test() throws Exception {
		List<ArticleMenu> list = articleMenuDao.getByParentId(0);
		System.out.println(JacksonUtil.writeValueAsString(list));
	}

}
