# Download the CSV from https://voaratinglists.blob.core.windows.net/html/rlidata.htm
curl --remote-name https://voaratinglists.blob.core.windows.net/downloads/uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.zip

unzip uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.zip

# Mongo DB importer only supports comma or tab delimited .csv files, but the .csv file is '*' delimited and contains commas in the text. So we need to clean it up a bit..

# Replace tabs with 3 spaces
#sed -i.orig 's/\t/   /g' uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.csv
sed --in-place 's/\t/   /g' uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.csv

# Replace * delimiters with tab delimiters
#sed -i.orig.no.tabs 's/*/\t/g' uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.csv
sed --in-place 's/*/\t/g' uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.csv

# Import as a tab delimited .tsv and give names to the different fields (fields described in doc https://voaratinglists.blob.core.windows.net/html/documents/2017%20Compiled%20List%20and%20SMV%20Data%20Specification.pdf)
mongoimport --db landtaxcalc --collection businessrates --type tsv --file uk-englandwales-ndr-2017-listentries-compiled-epoch-0002-baseline-csv.csv --fields "incrementing_entry_number,billing_authority_code,ndr_community_code,ba_reference_number,primary_and_secondary_description_code,primary_description_text,unique_address_reference_number_uarn,full_property_identifier,firms_name,number_or_name,street,town,postal_district,county,postcode,effective_date,composite_indicator,rateable_value,appeal_settlement_code,assessment_reference,list_alteration_date,scat_code_and_suffix,sub_street_level_3,sub_street_level_2,sub_street_level_1,case_number,current_from_date,current_to_date"