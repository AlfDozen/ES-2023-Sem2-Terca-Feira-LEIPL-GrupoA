package alfdozen.es_2023_2sem_terca_teira_leipl_grupoa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * @author alfdozen
 * 
 *         The Schedule class is used to represent a schedule of lectures for a
 *         student. It contains a list of Lectures and information about the
 *         student, such as their name and student number. The constructor can
 *         be used to create an empty schedule or a schedule with a list of
 *         lectures, as well as providing student information. The student
 *         number must be a positive integer and will be validated by the class.
 *         The class can also add or remove lectures to/from the schedule. The
 *         class can be sorted by the time slots of the lectures in the
 *         schedule. The toString() method returns a String representation of
 *         the schedule, including the student name and number, as well as the
 *         list of lectures. If the student name or number is not provided, the
 *         string "Unknown" will be used instead. If the schedule is empty, the
 *         string "Schedule is empty" will be returned.
 * 
 *
 * @version 1.0.0
 */
final class Schedule {
	static final String FOR_NULL = "Unknown";
	static final String NEGATIVE_EXCEPTION = "The studentNumber can't be negative";
	static final String NOT_NUMBER_EXCEPTION = "The provided string doesn't correspond to a number";
	static final String SAVE_FILE_EXCEPTION = "The file could not be saved";
	static final String READ_EXCEPTION = "Error: File read";
	static final String READ_WRITE_EXCEPTION = "Error: File read or write";
	static final String WRONG_FILE_FORMAT_EXCEPTION = "The file format should be ";
	static final String FILE_NULL_EXCEPTION = "The file cannot be null!";
	static final String ROW_EXCEPTION = "The row has more columns than the expected";
	static final String DELIMITER = ";";
	static final String FILE_FORMAT_CSV = ".csv";
	static final String HEADER = "Curso;Unidade Curricular;Turno;Turma;Inscritos no turno;Dia da semana;Hora início da aula;Hora fim da aula;Data da aula;Sala atribuída à aula;Lotação da sala";
	static final String EMPTY_ROW = ";;;;;;;;;;";
	static final Integer NUMBER_COLUMNS = 11;
	static final String FILE_FORMAT_JSON = ".json";
	static final String FILE_EXISTS_EXCEPTION = "The file already exists!";
	static final String FILE_NOT_EXISTS_EXCEPTION = "The provided file does not exist!";
	static final String DELIMITER_NULL_EXCEPTION = "The delimiter cannot be null!";
	static final String FOLDER_NOT_EXISTS_EXCEPTION = "The provided parent folder does not exist!";
	static final String FILE_MISSING_DATA = "At least 1 record of the data provided does not have the required values filled!";
	static final String ENCODE_TO = "ISO-8859-1";
	static final String ENCODE_FROM = "UTF-8";

	private List<Lecture> lectures;
	private String studentName;
	private Integer studentNumber;

	/**
	 * Default constructor creates an empty Schedule.
	 */
	Schedule() {
		this.studentName = null;
		this.studentNumber = null;
		this.lectures = new ArrayList<>();
	}

	/**
	 * Constructor creates a Schedule with a list of lectures.
	 * 
	 * @param lectures A list of Lecture objects.
	 */
	Schedule(List<Lecture> lectures) {
		this.studentName = null;
		this.studentNumber = null;
		setLectures(lectures);
	}

	/**
	 * Constructor creates a Schedule with a list of lectures and student
	 * information.
	 * 
	 * @param lectures      A list of Lecture objects.
	 * @param studentName   The name of the student.
	 * @param studentNumber The student number as an Integer.
	 * @throws IllegalArgumentException if the studentNumber is negative.
	 */
	Schedule(List<Lecture> lectures, String studentName, Integer studentNumber) {
		if (studentNumber != null && studentNumber < 0) {
			throw new IllegalArgumentException(NEGATIVE_EXCEPTION);
		}
		setLectures(lectures);
		this.studentName = studentName;
		this.studentNumber = studentNumber;
	}

	/**
	 * Constructor creates a Schedule with a list of lectures and student
	 * information.
	 * 
	 * @param lectures      A list of Lecture objects.
	 * @param studentName   The name of the student.
	 * @param studentNumber The student number as a String.
	 * @throws NumberFormatException    if the provided studentNumber is not a valid
	 *                                  number.
	 * @throws IllegalArgumentException if the studentNumber is negative.
	 */
	Schedule(List<Lecture> lectures, String studentName, String studentNumber) {
		setLectures(lectures);
		this.studentName = studentName;
		if (studentNumber == null) {
			this.studentNumber = null;
			return;
		}
		try {
			this.studentNumber = Integer.parseInt(studentNumber);
		} catch (NumberFormatException e) {
			throw new NumberFormatException(NOT_NUMBER_EXCEPTION);
		}
		if (this.studentNumber < 0) {
			throw new IllegalArgumentException(NEGATIVE_EXCEPTION);
		}
	}

	/**
	 * Constructor creates a Schedule with student information.
	 * 
	 * @param studentName   The name of the student.
	 * @param studentNumber The student number as an Integer.
	 */
	Schedule(String studentName, Integer studentNumber) {
		this(new ArrayList<>(), studentName, studentNumber);
	}

	/**
	 * Constructor creates a Schedule with student information.
	 * 
	 * @param studentName   The name of the student.
	 * @param studentNumber The student number as a String.
	 */
	Schedule(String studentName, String studentNumber) {
		this(new ArrayList<>(), studentName, studentNumber);
	}

	/**
	 * Returns a copy of the list of lectures in the schedule.
	 * 
	 * @return A list of Lecture objects.
	 */
	List<Lecture> getLectures() {
		return new ArrayList<>(this.lectures);
	}

	/**
	 * Sets the list of lectures in the schedule and sorts it.
	 * 
	 * @param lectures A list of Lecture objects.
	 */
	void setLectures(List<Lecture> lectures) {
		if (lectures == null) {
			this.lectures = new ArrayList<>();
		} else {
			this.lectures = new ArrayList<>(lectures);
			sortLectures();
		}
	}

	/**
	 * Sorts the list of lectures in the schedule by their time slots.
	 */
	void sortLectures() {
		Collections.sort(this.lectures);
	}

	/**
	 * Returns the student name associated with the schedule.
	 * 
	 * @return A string representing the student's name.
	 */
	String getStudentName() {
		return studentName;
	}

	/**
	 * Sets the student name associated with the schedule.
	 * 
	 * @param studentName A string representing the student's name.
	 */
	void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	/**
	 * Returns the student number associated with the schedule.
	 * 
	 * @return An integer representing the student's number.
	 */
	Integer getStudentNumber() {
		return studentNumber;
	}

	/**
	 * Sets the student number associated with the schedule.
	 * 
	 * @param studentNumber An integer representing the student's number.
	 * @throws IllegalArgumentException if the studentNumber is negative.
	 */
	void setStudentNumber(Integer studentNumber) {
		if (studentNumber != null && studentNumber < 0) {
			throw new IllegalArgumentException(NEGATIVE_EXCEPTION);
		}
		this.studentNumber = studentNumber;
	}

	/**
	 * Adds a lecture to the schedule and sorts the schedule.
	 * 
	 * @param lecture A Lecture object to be added to the schedule.
	 */
	void addLecture(Lecture lecture) {
		this.lectures.add(lecture);
		sortLectures();
	}

	/**
	 * Removes a lecture from the schedule.
	 * 
	 * @param lecture A Lecture object to be removed from the schedule.
	 * @throws IllegalArgumentException if the schedule doesn't contain the
	 *                                  specified lecture.
	 */
	void removeLecture(Lecture lecture) {
		if (!this.lectures.contains(lecture)) {
			throw new IllegalArgumentException("The schedule doesn't contain this lecture");
		}
		this.lectures.remove(lecture);
	}

	/**
	 * Method that saves a Schedule object to a CSV file.
	 * 
	 * @param schedule A schedule to be saved
	 * @param fileName A name for the CSV file
	 * @throws IOException if an I/O error occurs while writing to the file
	 */
	public static void saveToCSV(Schedule schedule, String fileName) throws IOException {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			String header = new String(HEADER.getBytes(ENCODE_FROM), ENCODE_TO);
			writer.write(header + "\n");
			for (Lecture lecture : schedule.getLectures()) {

				String[] attrArray = new String[11];
				attrArray[0] = lecture.getAcademicInfo().getDegree();
				attrArray[1] = lecture.getAcademicInfo().getCourse();
				attrArray[2] = lecture.getAcademicInfo().getShift();
				attrArray[3] = lecture.getAcademicInfo().getClassGroup();
				attrArray[5] = lecture.getTimeSlot().getWeekDay();
				attrArray[6] = lecture.getTimeSlot().getTimeBeginString();
				attrArray[7] = lecture.getTimeSlot().getTimeEndString();
				attrArray[8] = lecture.getTimeSlot().getDateString();
				attrArray[9] = lecture.getRoom().getName();

				for (int i = 0; i < attrArray.length; i++) {
					if (attrArray[i] == null || attrArray[i].equals(FOR_NULL)) {
						attrArray[i] = new String("".getBytes(ENCODE_FROM), ENCODE_TO);
					}
				}
				if (lecture.getAcademicInfo().getStudentsEnrolled() != null)
					attrArray[4] = new String(lecture.getAcademicInfo().getStudentsEnrolled().toString().getBytes(ENCODE_FROM), ENCODE_TO);

				if (lecture.getRoom().getCapacity() != null)
					attrArray[10] = new String(lecture.getRoom().getCapacity().toString().getBytes(ENCODE_FROM), ENCODE_TO);

				writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n", attrArray[0], attrArray[1],
						attrArray[2], attrArray[3], attrArray[4], attrArray[5], attrArray[6], attrArray[7],
						attrArray[8], attrArray[9], attrArray[10]));
			}
			writer.close();
		} catch (IOException e) {
			throw new IOException(SAVE_FILE_EXCEPTION);
		}
	}

	/**
	 * This method allows you to load a schedule via a csv file
	 *
	 * @param filePath the path of the csv file
	 * @return a schedule object with all existing lectures in the file given as
	 *         input
	 * @throws IllegalArgumentException is thrown if the file path is null
	 * @throws IllegalArgumentException is thrown if the file is in the wrong format
	 * @throws IOException              is thrown if an error is given when the file
	 *                                  is read
	 */
	static Schedule loadCSV(String filePath) throws IOException {
		if (filePath == null) {
			throw new IllegalArgumentException(FILE_NULL_EXCEPTION);
		}
		if (!filePath.endsWith(FILE_FORMAT_CSV) && !filePath.endsWith(FILE_FORMAT_CSV.toUpperCase())) {
			throw new IllegalArgumentException(WRONG_FILE_FORMAT_EXCEPTION + FILE_FORMAT_CSV);
		}
		Schedule schedule = new Schedule();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String lineRaw = "";
			while ((lineRaw = br.readLine()) != null) {
				String line = new String(lineRaw.getBytes(ENCODE_FROM), ENCODE_TO);
				if (!line.isBlank() && !line.equals(new String(HEADER.getBytes(ENCODE_FROM), ENCODE_TO)) && !line.equals(EMPTY_ROW)) {
					Lecture lecture = buildLecture(line);
					schedule.addLecture(lecture);
				}
			}
			br.close();
		} catch (IOException e) {
			throw new IOException(READ_EXCEPTION);
		}
		return schedule;
	}

	/**
	 * This method saves a Schedule object to a JSON file.
	 * 
	 * @param schedule A Schedule object to be saved.
	 * @param fileName A name for the file to save the Schedule object.
	 * 
	 * @throws IOException if an I/O error occurs while writing the JSON file.
	 */
	public static void saveToJSON(Schedule schedule, String fileName) throws IOException {

		List<Lecture> lectures = schedule.getLectures();
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode lecturesArray = mapper.createArrayNode();

		for (Lecture lecture : lectures) {
			ObjectNode lectureNode = mapper.createObjectNode();

			String[] attrArray = new String[11];
			attrArray[0] = lecture.getAcademicInfo().getDegree();
			attrArray[1] = lecture.getAcademicInfo().getCourse();
			attrArray[2] = lecture.getAcademicInfo().getShift();
			attrArray[3] = lecture.getAcademicInfo().getClassGroup();
			attrArray[5] = lecture.getTimeSlot().getWeekDay();
			attrArray[6] = lecture.getTimeSlot().getTimeBeginString();
			attrArray[7] = lecture.getTimeSlot().getTimeEndString();
			attrArray[8] = lecture.getTimeSlot().getDateString();
			attrArray[9] = lecture.getRoom().getName();

			for (int i = 0; i < attrArray.length; i++) {
				if (attrArray[i] == null || attrArray[i].equals(FOR_NULL)) {
					attrArray[i] = new String("".getBytes(ENCODE_FROM), ENCODE_TO);
					attrArray[i] = "";
				}
			}
			if (lecture.getAcademicInfo().getStudentsEnrolled() != null)
				//attrArray[4] = new String(lecture.getAcademicInfo().getStudentsEnrolled().toString().getBytes(ENCODE_FROM), ENCODE_TO);
				attrArray[4] = lecture.getAcademicInfo().getStudentsEnrolled().toString();
			
			if (lecture.getRoom().getCapacity() != null)
				//attrArray[10] = new String(lecture.getRoom().getCapacity().toString().getBytes(ENCODE_FROM), ENCODE_TO);
				attrArray[10] = lecture.getRoom().getCapacity().toString();

			lectureNode.put("Curso", attrArray[0]);
			lectureNode.put("Unidade Curricular", attrArray[1]);
			lectureNode.put("Turno", attrArray[2]);
			lectureNode.put("Turma", attrArray[3]);
			lectureNode.put("Inscritos no turno", attrArray[4]);
			lectureNode.put("Dia da semana", attrArray[5]);
			lectureNode.put("Hora inicio da aula", attrArray[6]);
			lectureNode.put("Hora fim da aula", attrArray[7]);
			lectureNode.put("Data da aula", attrArray[8]);
			lectureNode.put("Sala atribuída à aula", attrArray[9]);
			lectureNode.put("Lotação da sala", attrArray[10]);

			lecturesArray.add(lectureNode);
		}

		try {
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(lecturesArray);
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(json);
			fileWriter.close();
		} catch (IOException e) {
			throw new IOException(SAVE_FILE_EXCEPTION);
		}
	}

	/**
	 * This method build a lecture object from a csv file entry
	 *
	 * @param line a csv file entry
	 * @return a lecture object from the csv file entry given as input
	 * @throws IllegalStateException is thrown if the number of columns is greater
	 *                               than expected
	 */
	private static Lecture buildLecture(String line) {
		String[] tempArr = line.split(DELIMITER);
		if (tempArr.length > NUMBER_COLUMNS) {
			throw new IllegalStateException(ROW_EXCEPTION);
		}
		String[] finalArr = new String[NUMBER_COLUMNS];
		if (tempArr.length == NUMBER_COLUMNS) {
			finalArr = buildLine(tempArr);
		}
		if (tempArr.length < NUMBER_COLUMNS) {
			finalArr = buildLine(tempArr);
			for (int i = tempArr.length; i < NUMBER_COLUMNS; i++) {
				finalArr[i] = null;
			}
		}
		AcademicInfo academicInfo = new AcademicInfo(finalArr[0], finalArr[1], finalArr[2], finalArr[3], finalArr[4]);
		TimeSlot timeSlot = new TimeSlot(finalArr[5], finalArr[8], finalArr[6], finalArr[7]);
		Room room = new Room(finalArr[9], finalArr[10]);
		return new Lecture(academicInfo, timeSlot, room);
	}

	/**
	 * This method allows you to replace empty fields in the csv file entry with
	 * null values
	 *
	 * @param array vector with the parsed csv file entry
	 * @return returns a String[]
	 */
	private static String[] buildLine(String[] array) {
		String[] finalArr = new String[NUMBER_COLUMNS];
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals("")) {
				finalArr[i] = null;
			} else {
				finalArr[i] = array[i];
			}
		}
		return finalArr;
	}

	/**
	 * This method allows to load a schedule via a json file
	 * 
	 * @param path the file path of the json file
	 * @return a schedule object with all existing lectures in the file given as
	 *         input
	 * @throws IllegalArgumentException is thrown if the file path is null
	 * @throws IllegalArgumentException is thrown if the file is in the wrong format
	 * @throws IOException              is thrown if an error is given when the file
	 *                                  is read
	 */
	@SuppressWarnings("unchecked")
	static Schedule loadJSON(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException(FILE_NULL_EXCEPTION);
		}
		if (!path.endsWith(FILE_FORMAT_JSON) && !path.endsWith(FILE_FORMAT_JSON.toUpperCase())) {
			throw new IllegalArgumentException(WRONG_FILE_FORMAT_EXCEPTION + FILE_FORMAT_JSON);
		}
		Schedule schedule = new Schedule();
		//String header = new String(HEADER.getBytes(ENCODE_FROM), ENCODE_TO);
		String[] headerArr = HEADER.split(DELIMITER);
		try {
			Reader reader = new InputStreamReader(new FileInputStream(path));
			ObjectMapper jsonMapper = new ObjectMapper();
			List<Object> data = jsonMapper.readValue(reader, List.class);
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> entry = (Map<String, Object>) data.get(i);
				Lecture lecture = buildLecture(entry, headerArr);
				schedule.addLecture(lecture);
			}
		} catch (IOException e) {
			throw new IOException(READ_EXCEPTION);
		}
		return schedule;
	}

	/**
	 * This method build a lecture object from a json file entry
	 * 
	 * @param entry       json entry in the form of a Map
	 * @param headerArray vector with the parsed header
	 * @return a lecture object from the json file entry given as input
	 */
	private static Lecture buildLecture(Map<String, Object> entry, String[] headerArr) throws IOException {
		String[] finalArr = new String[NUMBER_COLUMNS];
		for (int j = 0; j < NUMBER_COLUMNS; j++) {
			String aux = (String) entry.get(headerArr[j]);
			//System.out.println(aux);
			if (aux.equals("")) {
				finalArr[j] = null;
			} else {
				finalArr[j] = aux;
//				try {
//					//finalArr[j] = new String(aux.getBytes(ENCODE_FROM), ENCODE_TO);
//					
//				} catch (UnsupportedEncodingException e) {
//					throw new IOException(READ_EXCEPTION);
//				}
			}
		}
		AcademicInfo academicInfo = new AcademicInfo(finalArr[0], finalArr[1], finalArr[2], finalArr[3], finalArr[4]);
		TimeSlot timeSlot = new TimeSlot(finalArr[5], finalArr[8], finalArr[6], finalArr[7]);
		Room room = new Room(finalArr[9], finalArr[10]);
		return new Lecture(academicInfo, timeSlot, room);
	}

	/**
	 * Converts a CSV file to a JSON file.
	 * 
	 * @param csvSourcePath       the path of the CSV file to convert.
	 * @param jsonDestinationPath the path of the JSON file to create.
	 * @param delimiter           the delimiter character used in the CSV file.
	 * @throws IOException              if there is an I/O error reading or writing
	 *                                  the files.
	 * @throws IllegalArgumentException if any of the input arguments are null or
	 *                                  invalid.
	 */
	static void convertCSV2JSON(String csvSourcePath, String jsonDestinationPath, Character delimiter)
			throws IOException {
		validateArguments(csvSourcePath, jsonDestinationPath, delimiter, FILE_FORMAT_CSV, FILE_FORMAT_JSON);

		File csvFile = new File(csvSourcePath);
		File jsonFile = new File(jsonDestinationPath);

		if (!csvFile.isFile())
			throw new IllegalArgumentException(FILE_NOT_EXISTS_EXCEPTION);
		if (!jsonFile.getParentFile().isDirectory())
			throw new IllegalArgumentException(FOLDER_NOT_EXISTS_EXCEPTION);
		if (jsonFile.isFile())
			throw new IllegalArgumentException(FILE_EXISTS_EXCEPTION);

		String destinationTempFilePath = "src/resources/tmpfile.csv";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationTempFilePath));
				BufferedReader reader = new BufferedReader(new FileReader(csvSourcePath));) {

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isBlank()) {
					writer.append(line);
					writer.newLine();
				}
			}
		}
		try (InputStream inputStream = new FileInputStream(destinationTempFilePath)) {

			CsvMapper csvMapper = new CsvMapper();
			CsvSchema csvSchema = CsvSchema.builder().setColumnSeparator(delimiter).setUseHeader(true).build();

			List<Object> csvData = csvMapper.readerFor(Map.class).with(csvSchema)
					.readValues(new InputStreamReader(inputStream)).readAll();

			Files.deleteIfExists(Paths.get(destinationTempFilePath));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, csvData);
		} catch (IOException e) {
			throw new IOException(READ_WRITE_EXCEPTION);
		}
	}

	/**
	 * Converts a JSON file to a CSV file.
	 * 
	 * @param jsonSourcePath     the path of the JSON file to convert.
	 * @param csvDestinationPath the path of the CSV file to create.
	 * @param delimiter          the delimiter character used in the CSV file.
	 * @throws IOException              if there is an I/O error reading or writing
	 *                                  the files.
	 * @throws IllegalArgumentException if any of the input arguments are null or
	 *                                  invalid.
	 */
	static void convertJSON2CSV(String jsonSourcePath, String csvDestinationPath, Character delimiter)
			throws IOException {
		validateArguments(jsonSourcePath, csvDestinationPath, delimiter, FILE_FORMAT_JSON, FILE_FORMAT_CSV);

		File jsonFile = new File(jsonSourcePath);
		File csvFile = new File(csvDestinationPath);

		if (!jsonFile.isFile())
			throw new IllegalArgumentException(FILE_NOT_EXISTS_EXCEPTION);
		if (!csvFile.getParentFile().isDirectory())
			throw new IllegalArgumentException(FOLDER_NOT_EXISTS_EXCEPTION);
		if (csvFile.isFile())
			throw new IllegalArgumentException(FILE_EXISTS_EXCEPTION);

		try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile));
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(csvFile))) {

			ObjectMapper jsonMapper = new ObjectMapper();
			List<Object> data = jsonMapper.readValue(reader, List.class);

			CsvMapper csvMapper = new CsvMapper();

			Map<String, Object> firstRecord = (Map<String, Object>) data.get(0);
			CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder().setUseHeader(true).disableQuoteChar();

			for (String columnName : firstRecord.keySet())
				csvSchemaBuilder.addColumn(columnName);

			CsvSchema csvSchema = csvSchemaBuilder.build().withColumnSeparator(delimiter);

			csvMapper.writerFor(List.class).with(csvSchema).writeValue(writer, data);

		} catch (Exception e) {
			throw new IOException(READ_WRITE_EXCEPTION);
		}
	}

	/**
	 * Validates the input arguments for file conversion methods.
	 *
	 * @param sourcePath        the path of the source file to be converted.
	 * @param destinationPath   the path of the destination file to be created.
	 * @param delimiter         the delimiter character used in the CSV file (if
	 *                          applicable).
	 * @param sourceFormat      the expected file format of the source file (e.g.,
	 *                          ".csv", ".json").
	 * @param destinationFormat the expected file format of the destination file
	 *                          (e.g., ".csv", ".json").
	 * @throws IllegalArgumentException if any of the input arguments are null or
	 *                                  invalid.
	 */
	static void validateArguments(String sourcePath, String destinationPath, Character delimiter, String sourceFormat,
			String destinationFormat) {
		if (sourcePath == null || destinationPath == null)
			throw new IllegalArgumentException(FILE_NULL_EXCEPTION);
		if (delimiter == null)
			throw new IllegalArgumentException(DELIMITER_NULL_EXCEPTION);
		if (!sourcePath.endsWith(sourceFormat))
			throw new IllegalArgumentException(WRONG_FILE_FORMAT_EXCEPTION + sourceFormat);
		if (!destinationPath.endsWith(destinationFormat))
			throw new IllegalArgumentException(WRONG_FILE_FORMAT_EXCEPTION + destinationFormat);
	}

	/**
	 * Returns a string representation of the schedule, including the student's name
	 * and number, and the list of lectures.
	 * 
	 * @return A string representing the schedule.
	 */
	@Override
	public String toString() {
		String str = "";
		if (studentName == null) {
			str += "Unknown Student Name\n";
		} else {
			str += "Student Name: " + studentName + "\n";
		}
		if (studentNumber == null) {
			str += "Unknown Student Number\n";
		} else {
			str += "Student Number: " + studentNumber + "\n";
		}
		if (lectures.isEmpty()) {
			str += "Schedule is empty";
		} else {
			str += "Schedule:\n";
			for (Lecture lecture : lectures) {
				str += lecture + "\n";
			}
		}
		return str;
	}
}
