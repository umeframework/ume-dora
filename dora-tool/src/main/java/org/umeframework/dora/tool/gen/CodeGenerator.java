/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen;

import java.io.File;
import java.io.FileWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * EntityGenerator
 */
public class CodeGenerator {
    /**
     * template
     */
    private Template template;

    /**
     * CodeGenerator
     * 
     * @param vltTemplate
     * @param charSet
     */
    public CodeGenerator(String vltTemplate, String charSet) {
        // Off Velocity log
        Velocity.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
        // Init Velocity log
        Velocity.init();
        template = Velocity.getTemplate(vltTemplate, charSet);
    }

    /**
     * CodeGenerator
     * 
     * @param vltTemplate
     */
    public CodeGenerator(String vltTemplate) {
        this(vltTemplate, "UTF-8");

    }

    /**
     * execute
     * 
     * @param dtoMap
     * @param genSrcPath
     * @param vltTemplate
     * @param charSet
     * @throws Exception
     */
    public void execute(String inputDtoName, Object inputDto, String fileName) throws Exception {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            VelocityContext context = new VelocityContext();
            context.put(inputDtoName, inputDto);
            template.merge(context, writer);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * createPackageDir
     * 
     * @param rootPath
     * @param packageName
     * @return
     */
    public String createPackageDir(String rootPath, String packageName) {
        rootPath = rootPath.contains("\\") ? rootPath.replace("\\", "/") : rootPath;
        rootPath = rootPath.endsWith("/") ? rootPath : rootPath + "/";
        packageName = packageName.replace(".", "/");
        packageName = rootPath + packageName;
        File p = new File(packageName);
        if (!p.exists()) {
            p.mkdirs();
        }
        return p.getPath() + "/";
    }

    /**
     * createPackageDir
     * 
     * @param rootPath
     * @param packageName
     * @return
     */
    public String createCSharpPackageDir(String rootPath) {
        rootPath = rootPath.contains("\\") ? rootPath.replace("\\", "/") : rootPath;
        rootPath = rootPath.endsWith("/") ? rootPath : rootPath + "/";
        File p = new File(rootPath);
        if (!p.exists()) {
            p.mkdirs();
        }
        return p.getPath() + "/";
    }
}
