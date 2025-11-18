package models;

import java.util.Map;

public record monedasConversion(
        String result,
        String time_last_update_utc,
        String base_code,
        Map<String, Double> conversion_rates
    )
{

}
