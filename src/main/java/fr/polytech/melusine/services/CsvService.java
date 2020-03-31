package fr.polytech.melusine.services;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import fr.polytech.melusine.exceptions.InternalServerErrorException;
import fr.polytech.melusine.exceptions.errors.SystemError;
import fr.polytech.melusine.mappers.UserMapper;
import fr.polytech.melusine.models.dtos.requests.UserCsvRequest;
import fr.polytech.melusine.models.entities.User;
import fr.polytech.melusine.models.enums.Section;
import fr.polytech.melusine.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static fr.polytech.melusine.utils.MoneyFormatter.formatToDouble;

@Service
public class CsvService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private Clock clock;

    public CsvService(UserRepository userRepository, UserMapper userMapper, Clock clock) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.clock = clock;
    }

    public void importUsers(MultipartFile csv, HttpServletResponse response) throws Exception {
        try (
                Reader reader = new BufferedReader(new InputStreamReader(csv.getInputStream()))
        ) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(UserCsvRequest.class);
            String[] columns = new String[]{"nom", "prenom", "surnom", "annee", "solde"};
            strategy.setColumnMapping(columns);

            List userCsvRequests = new CsvToBeanBuilder<UserCsvRequest>(reader)
                    .withSkipLines(1)
                    .withType(UserCsvRequest.class)
                    .withMappingStrategy(strategy)
                    .build().parse();

            String report = "rapport.csv";
            String[] header = {"Nom", "Prénom", "Surnom", "Année", "Solde", "Enregistré", "Raison"};
            CSVWriter csvWriter = getCsvWriter(response, report, header);

            for (Object object : userCsvRequests) {
                UserCsvRequest userCsvRequest = (UserCsvRequest) object;
                Section section = Section.valueOf(userCsvRequest.getAnnee());
                double credit = Double.parseDouble(userCsvRequest.getSolde());
                if (!userRepository.existsByFirstNameAndLastNameAndSection(userCsvRequest.getPrenom(), userCsvRequest.getNom(), section)) {
                    User user = userMapper.mapToUser(userCsvRequest, section, credit, clock);
                    userRepository.save(user);
                    csvWriter.writeNext(new String[]{
                            user.getLastName(),
                            user.getFirstName(),
                            Objects.nonNull(user.getNickName()) ? user.getNickName() : "",
                            user.getSection().toString(),
                            String.valueOf(formatToDouble(user.getCredit())),
                            user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            "Oui",
                            ""
                    });
                } else {
                    csvWriter.writeNext(new String[]{
                            userCsvRequest.getNom(),
                            userCsvRequest.getPrenom(),
                            Objects.nonNull(userCsvRequest.getSurnom()) ? userCsvRequest.getSurnom() : "",
                            userCsvRequest.getAnnee(),
                            userCsvRequest.getSolde(),
                            "",
                            "Non",
                            "L'utilisateur renseigné existe déjà"
                    });
                }
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(SystemError.TECHNICAL_ERROR, e);
        }

    }

    public void exportUsers(HttpServletResponse response) throws Exception {
        String filename = "users.csv";
        String[] header = {"Nom", "Prénom", "Surnom", "Année", "Solde", "Date de création"};
        CSVWriter csvWriter = getCsvWriter(response, filename, header);

        Iterable<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName"));

        users.forEach(user -> {
            csvWriter.writeNext(new String[]{
                    user.getLastName(),
                    user.getFirstName(),
                    Objects.nonNull(user.getNickName()) ? user.getNickName() : "",
                    user.getSection().toString(),
                    String.valueOf(formatToDouble(user.getCredit())),
                    user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))});
        });
        csvWriter.close();
    }

    private CSVWriter getCsvWriter(HttpServletResponse response, String filename, String[] headerRecord) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        CSVWriter csvWriter = new CSVWriter(response.getWriter(),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        csvWriter.writeNext(headerRecord);
        return csvWriter;
    }

}
