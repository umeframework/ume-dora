/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.umeframework.dora.tool.gen.service.DocBean;
import org.umeframework.dora.tool.gen.service.DocDtoBean;
import org.umeframework.dora.tool.gen.service.DocFuncBean;
import org.umeframework.dora.tool.gen.service.DocItemBean;
import org.umeframework.dora.tool.gen.service.ServiceBuilder;
import org.umeframework.dora.tool.gen.service.ServiceExcelParser;
import org.umeframework.dora.tool.poi.TypeMapper;

/**
 * ServiceGenerator
 *
 * @author MA YUE
 */
public class ServiceGenerator {
    // generate file DIR setting
    private String genDirJava = "src/main/gen/";
    private String templateServiceDto = "template/service-dto.vm";
    private String templateServiceInterface = "template/service-interface.vm";
    /**
     * TypeMapper instance
     */
    private TypeMapper typeMapper;
    
    /**
     * ServiceGenerator
     */
    public ServiceGenerator(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    /**
     * execute
     *
     * @throws Exception
     */
    public List<String> execute(String inputPath) throws Exception {
        File[] pathnames = new File(inputPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File e) {
                return e.isDirectory();
            }
        });
        List<File> files = new ArrayList<File>();

        for (File pathname : pathnames) {
            File[] defines = pathname.listFiles(new FileFilter() {
                @Override
                public boolean accept(File e) {
                    String name = e.getName().toLowerCase();
                    return name.endsWith(".xls") || name.endsWith(".xlsx");
                }
            });
            for (File define : defines) {
                files.add(define);
            }
        }

        // Get all declare Dto list
        Map<String, String> declareDtoMap = new LinkedHashMap<String, String>();
        for (File file : files) {
            Map<String, String> map = new ServiceExcelParser().getDeclareDtoListOfSamePackage(file);
            declareDtoMap.putAll(map);
        }
        Set<String> declareDtoList = declareDtoMap.keySet();

        // Parse for generate
        CodeGenerator dtoGen = new CodeGenerator(templateServiceDto, "UTF-8");
        CodeGenerator sifGen = new CodeGenerator(templateServiceInterface, "UTF-8");

        for (File file : files) {
            // Parse excel and create DocBean
            DocBean docBean = new ServiceExcelParser().parseWorkBook(file);
            // create appendix information for DocBean
            new ServiceBuilder(typeMapper).build(docBean, declareDtoList);

            // Generate regular service Dto
            for (DocDtoBean dtoBean : docBean.getDtoBeanList()) {
                String pkg = dtoBean.getPkg();
                String fileName = dtoGen.createPackageDir(genDirJava, pkg) + dtoBean.getId() + ".java";
                dtoGen.execute("dtoBean", dtoBean, fileName);
                System.out.println("[" + fileName + "] created.");
            }

            if (docBean.getCmpBean() == null) {
                continue;
            }

            // Generate Service Interface
            String pkg = docBean.getCmpBean().getPkg();
            String className = docBean.getCmpBean().getId() + ".java";
            String fileName = dtoGen.createPackageDir(genDirJava, pkg) + className;
            sifGen.execute("docBean", docBean, fileName);
            System.out.println("[" + fileName + "] created.");

            // Generate Service Control for MVC layer
            if (!docBean.getCmpBean().hasWS()) {
                continue;
            }

            // Generate Service Control In/Out Dto for MVC layer
            pkg = docBean.getCmpBean().getPkg() + ".web";
            for (DocItemBean cmpBeanItemBean : docBean.getCmpBean().getFuncList()) {
                String wsFlag = cmpBeanItemBean.getWsFlag();
                if (wsFlag == null || !wsFlag.equals("YES")) {
                    continue;
                }

                for (DocFuncBean funcBean : docBean.getFuncBeanList()) {
                    if (!cmpBeanItemBean.getId().equals(funcBean.getId())) {
                        continue;
                    }
                    funcBean.setPkg(pkg);
                    String wsId = docBean.getCmpBean().getWSID(funcBean.getId());
                    funcBean.setWsId(wsId);

                }

            }

        }

        return null;
    }

    /**
     * @return the genDirJava
     */
    public String getGenDirJava() {
        return genDirJava;
    }

    /**
     * @param genDirJava
     *            the genDirJava to set
     */
    public void setGenDirJava(String genDirJava) {
        this.genDirJava = genDirJava;
    }

    /**
     * @return the templateServiceDto
     */
    public String getTemplateServiceDto() {
        return templateServiceDto;
    }

    /**
     * @param templateServiceDto
     *            the templateServiceDto to set
     */
    public void setTemplateServiceDto(String templateServiceDto) {
        this.templateServiceDto = templateServiceDto;
    }

    /**
     * @return the templateServiceInterface
     */
    public String getTemplateServiceInterface() {
        return templateServiceInterface;
    }

    /**
     * @param templateServiceInterface
     *            the templateServiceInterface to set
     */
    public void setTemplateServiceInterface(String templateServiceInterface) {
        this.templateServiceInterface = templateServiceInterface;
    }

}
