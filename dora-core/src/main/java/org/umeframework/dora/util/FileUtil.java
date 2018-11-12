/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * File operation tool
 * 
 * @author Yue MA
 */
abstract public class FileUtil {
	/**
	 * DOT char const
	 */
	private static final char DOT_CHR = '.';
	/**
	 * EMPTY char const
	 */
	private static final String EMPTY = "";

	/**
	 * Properties file loader
	 * 
	 * @param configFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static Properties loadConfig(String configFile) throws IOException {
		Properties prop = new Properties();
		prop.load(new java.io.FileInputStream(configFile));
		return prop;
	}

	/**
	 * List all files from file path
	 * 
	 * @param rootPaths
	 * @param fileType
	 * @return
	 */
	public static List<File> listFiles(String rootPaths, final String fileType) {
		rootPaths = rootPaths.contains("\\\\") ? rootPaths.replace("\\\\", "/") : rootPaths;
		rootPaths = rootPaths.contains("\\") ? rootPaths.replace("\\", "/") : rootPaths;
		String[] paths = null;
		if (rootPaths.contains(";")) {
			paths = rootPaths.split(";");
		} else {
			paths = new String[] { rootPaths };
		}

		List<File> result = new ArrayList<File>();
		for (String path : paths) {
			listFiles(result, new File(path), fileType, null);
		}
		return result;
	}

	/**
	 * List all files from file path
	 * 
	 * @param rootPaths
	 * @param fileType
	 * @param exclueFileTypes
	 * @return
	 */
	public static List<File> listFiles(String rootPaths, final String fileType, final String[] exclueFileTypes) {
		rootPaths = rootPaths.contains("\\\\") ? rootPaths.replace("\\\\", "/") : rootPaths;
		rootPaths = rootPaths.contains("\\") ? rootPaths.replace("\\", "/") : rootPaths;
		String[] paths = null;
		if (rootPaths.contains(";")) {
			paths = rootPaths.split(";");
		} else {
			paths = new String[] { rootPaths };
		}

		List<File> result = new ArrayList<File>();
		for (String path : paths) {
			listFiles(result, new File(path), fileType, exclueFileTypes);
		}
		return result;
	}

	/**
	 * List all files from file path
	 * 
	 * @param result
	 * @param path
	 * @param fileType
	 */
	private static void listFiles(List<File> result, File path, final String fileType, final String[] exclueFiles) {
		File[] files = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (!f.isFile()) {
					return false;
				}
				if (fileType == null || fileType.trim().equals("")) {
					return true;
				}
				String fileExName = fileType.toUpperCase();
				if (!fileExName.startsWith(".")) {
					fileExName = "." + fileExName;
				}
				if (f.getName().toUpperCase().endsWith(fileExName)) {
					if (exclueFiles != null) {
						for (String exclueFileType : exclueFiles) {
							if (f.getName().equalsIgnoreCase(exclueFileType)) {
								return false;
							}
						}
					}
					return true;
				}
				return false;
			}
		});

		if (files != null) {
			for (File f : files) {
				result.add(f);
			}
		}

		File[] subPaths = path.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				return false;
			}
		});

		if (subPaths != null) {
			for (File p : subPaths) {
				listFiles(result, p, fileType, exclueFiles);
			}
		}
	}

	/**
	 * removeDirs
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean removeDirs(File dir) {
		if (dir == null) {
			return false;
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					removeDirs(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return dir.delete();
	}

	/**
	 * copyFile
	 * 
	 * @param srcFile
	 * @param newFile
	 * @throws IOException 
	 */
	public static void copyFile(String srcFile, String newFile) throws IOException {
		copyFile(srcFile, newFile, 2048);

	}

	/**
	 * copyFile
	 * 
	 * @param srcFile
	 * @param newFile
	 * @param bufferSize
	 */
	public static void copyFile(String srcFile, String newFile, int bufferSize) throws IOException {

		checkAbsolutePath(srcFile);
		checkAbsolutePath(newFile);

		File srcFileObject = new File(srcFile);
		if (!srcFileObject.exists()) {
			throw new IOException(srcFile + " is not exist.");
		}

		File newFileObject = new File(newFile);
		if (newFileObject.exists()) {
			deleteFile(newFile);
		} else if (newFileObject.exists()) {
			throw new IOException(newFile + " is exist.");
		}

		FileOutputStream fos = null;
		FileChannel outputFileChannel = null;
		FileInputStream fis = null;
		FileChannel inputFileChannel = null;
		FileLock inputFileLock = null;
		FileLock outputFileLock = null;
		try {
			fos = new FileOutputStream(newFileObject, true);
			outputFileChannel = fos.getChannel();

			fis = new FileInputStream(srcFileObject);
			inputFileChannel = fis.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

			inputFileLock = inputFileChannel.lock(0L, Long.MAX_VALUE, true);
			outputFileLock = outputFileChannel.lock(0L, Long.MAX_VALUE, false);

			while (inputFileChannel.position() < inputFileChannel.size()) {
				buffer.clear();
				inputFileChannel.read(buffer);
				buffer.flip();
				outputFileChannel.write(buffer);
			}
		} catch (IOException e) {
			throw new IOException("Failed to copy file ", e);
		} finally {
			try {
				if (inputFileLock != null) {
					inputFileLock.release();
				}
				if (outputFileLock != null) {
					outputFileLock.release();
				}

				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}

				if (outputFileChannel != null) {
					outputFileChannel.close();
				}

				if (inputFileChannel != null) {
					inputFileChannel.close();
				}
			} catch (IOException e) {
				throw new IOException("Failed to close file ", e);
			}
		}
	}

	/**
	 * deleteFile
	 * 
	 * @param srcFile
	 * @throws IOException 
	 */
	public static void deleteFile(String srcFile) throws IOException {

		checkAbsolutePath(srcFile);

		File srcFileObject = new File(srcFile);

		if (!srcFileObject.exists()) {
			throw new IOException(srcFile + " is not exist.");
		}

		boolean result = srcFileObject.delete();

		if (!result) {
			throw new IOException("File control operation was failed.");
		}
	}

	/**
	 * renameFile
	 * 
	 * @param srcFile
	 * @param newFile
	 * @throws IOException 
	 */
	public static void renameFile(String srcFile, String newFile) throws IOException {

		checkAbsolutePath(srcFile);
		checkAbsolutePath(newFile);

		File srcFileObject = new File(srcFile);
		File newFileObject = new File(newFile);

		if (!srcFileObject.exists()) {
			throw new IOException(srcFile + " is not exist.");
		}

		if (newFileObject.exists()) {
			deleteFile(newFile);
		} else if (newFileObject.exists()) {
			throw new IOException(newFile + " is exist.");
		}

		boolean result = true;
		result = srcFileObject.renameTo(newFileObject);

		if (!result) {
			throw new IOException("File control operation was failed.");
		}
	}

	/**
	 * mergeFile
	 * 
	 * @param fileList
	 * @param newFile
	 * @throws IOException 
	 */
	public static void mergeFile(List<String> fileList, String newFile) throws IOException {
		File newFileObject = new File(newFile);

		checkAbsolutePath(newFile);

		if (newFileObject.exists()) {
			deleteFile(newFile);
		} else if (newFileObject.exists()) {
			throw new IOException(newFile + " is exist.");
		}

		FileOutputStream fos = null;
		FileChannel outputFileChannel = null;
		FileLock outputFileLock = null;

		try {
			fos = new FileOutputStream(newFileObject, true);
			outputFileChannel = fos.getChannel();
			outputFileLock = outputFileChannel.lock(0L, Long.MAX_VALUE, false);

			for (String srcFile : fileList) {

				checkAbsolutePath(srcFile);

				File srcFileObject = new File(srcFile);

				if (!srcFileObject.exists()) {
					throw new IOException(srcFile + " is not exist.");
				}

				FileInputStream fis = null;
				FileChannel inputFileChannel = null;
				FileLock inputFileLock = null;

				try {
					fis = new FileInputStream(srcFileObject);
					inputFileChannel = fis.getChannel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);

					inputFileLock = inputFileChannel.lock(0L, Long.MAX_VALUE, true);

					while (inputFileChannel.position() < inputFileChannel.size()) {
						buffer.clear();
						inputFileChannel.read(buffer);
						buffer.flip();
						outputFileChannel.write(buffer);
					}
				} finally {
					if (inputFileLock != null) {
						inputFileLock.release();
					}
					if (fis != null) {
						fis.close();
					}
					if (inputFileChannel != null) {
						inputFileChannel.close();
					}
				}

			}
		} catch (IOException e) {
			throw new IOException("Failed to merge file ", e);
		} finally {
			try {
				if (outputFileLock != null) {
					outputFileLock.release();
				}

				if (fos != null) {
					fos.close();
				}

				if (outputFileChannel != null) {
					outputFileChannel.close();
				}

			} catch (IOException e) {
				throw new IOException("Failed to close file ", e);
			}

		}
	}

	/**
	 * checkAbsolutePath
	 * 
	 * @param filePath
	 * @throws IOException 
	 */
	private static void checkAbsolutePath(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.isAbsolute()) {
			throw new IOException("File path is not absolute.");
		}
	}

	/**
	 * Get file extension
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(String file) {
		int index = file.lastIndexOf(DOT_CHR);
		return (index < 0) ? EMPTY : file.substring(index);
	}

	/**
	 * Get file extension
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	/**
	 * Read file as byte array
	 * 
	 * @param srcFile
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getFileContent(String srcFile) throws IOException {
		return getFileContent(srcFile, 4096);
	}

	/**
	 * Read file as byte array
	 * 
	 * @param srcFile
	 * @param bufferSize
	 * @return
	 * @throws IOException 
	 */
	public static byte[] getFileContent(String srcFile, int bufferSize) throws IOException {
		checkAbsolutePath(srcFile);
		File srcFileObject = new File(srcFile);
		if (!srcFileObject.exists()) {
			throw new IOException(srcFile + " is not exist.");
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(srcFileObject);
			List<byte[]> blockList = new ArrayList<byte[]>();
			byte[] block = new byte[bufferSize];
			int size = fis.read(block);
			while (size > 0) {
				blockList.add(block);
				block = new byte[bufferSize];
				size = fis.read(block);
			}
			byte[] destBlock = new byte[bufferSize * blockList.size()];
			int destPos = 0;
			for (int i = 0; i < blockList.size(); i++) {
				block = blockList.get(i);
				destPos = bufferSize * i;
				System.arraycopy(block, 0, destBlock, destPos, block.length);
			}
			return destBlock;
		} catch (Exception e) {
			throw new IOException("Failed to read file ", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				throw new IOException("Failed to close file ", e);
			}
		}
	}

}
