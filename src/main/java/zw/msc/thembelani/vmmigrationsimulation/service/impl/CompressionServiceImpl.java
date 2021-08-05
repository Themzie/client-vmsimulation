package zw.msc.thembelani.vmmigrationsimulation.service.impl;

import zw.msc.thembelani.vmmigrationsimulation.service.CompressionService;

public class CompressionServiceImpl implements CompressionService {
    @Override
    public String encode(String input) {

        StringBuilder outputString = new StringBuilder();

        int count = 1;
        for (int i = 0; i < input.length(); i++) {

            // resetting to 1 for every new character (counting current character).
            count = 1;
            while (i < input.length() - 1 && input.charAt(i) == input.charAt(i + 1)) {
                count++;
                i++;
            }
            outputString.append(input.charAt(i)).append(count);
        }
        return outputString.toString();
    }
}
